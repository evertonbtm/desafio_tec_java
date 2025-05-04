package br.com.batista.desafio01.service.notification;

import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.UnavailableException;
import br.com.batista.desafio01.model.dto.AuthorizeDTO;
import br.com.batista.desafio01.model.dto.NotifyDTO;
import br.com.batista.desafio01.model.entities.Notification;
import br.com.batista.desafio01.model.entities.Transaction;
import br.com.batista.desafio01.model.enums.ENotification;
import br.com.batista.desafio01.repository.INotificationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private WebClient webClient;

    private final INotificationRepository notificationRepository;

    private final MessageService messageService;

    public NotificationService(INotificationRepository notificationRepository, MessageService messageService) {
        this.notificationRepository = notificationRepository;
        this.messageService = messageService;
    }

    @Value("${notify.api.url}")
    private String apiUrl;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder().baseUrl(apiUrl).build();
    }

    @Transactional
    public Notification save(Notification notification){
        return notificationRepository.save(notification);
    }

    public Mono<NotifyDTO> callNotificationApi(AuthorizeDTO params) {
        return webClient.post()
                .uri("/posts")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(NotifyDTO.class);
    }


    @Override
    public void notify(Transaction transaction) throws UnavailableException {
        AuthorizeDTO params = new AuthorizeDTO();
        long seconds = Instant.now().getEpochSecond();
        //aleatoridade para simular indisponibilidade.
        String result = (seconds % 2 == 0) ? "error" : "success";
        params.setStatus(result);

        try {
            // mock api
            NotifyDTO notifyDTO = callNotificationApi(params).block();
            if(notifyDTO != null && notifyDTO.getStatus().equals("success")){
                saveNotification(transaction, true);
                logger.debug(messageService.getMessage("notify.message.success"));
            }else{
                notifyQueue(transaction);
                logger.debug(messageService.getMessage("notify.message.error"));
                throw new UnavailableException(Transaction.class, messageService.getMessage("notify.unavailable"));
            }
        } catch (Exception e){
            notifyQueue(transaction);
            throw new UnavailableException(Transaction.class, messageService.getMessage("notify.unavailable"));
        }

    }

    private void saveNotification(Transaction transaction, boolean isSent) {
        var notification = findByTransaction(transaction.getIdTransaction());

        if(notification == null){
            notification = new Notification();
            notification.setUserEmail(transaction.getPayee().getEmail());
            notification.setSent(isSent);
        }

        notification.setCreateDate(new Date());
        notification.setTitle(ENotification.TITLE.get());
        notification.setMessage(ENotification.MESSAGE.get()
                .replace("{0}",String.valueOf(transaction.getValue()))
                .replace("{1}", transaction.getPayer().getDocument()));

        notification.setTransaction(transaction);

        save(notification);
    }

    public Notification findByTransaction(long idTransation) {
        List<Notification> notificationList = notificationRepository.findListByTransactionId(idTransation);

        if(notificationList == null || notificationList.isEmpty()){
            return null;
        }

        if(notificationList.size() > 1){
            throw new FieldDuplicatedException(Notification.class, "transaction", "");
        }

        return notificationList.get(0);
    }

    private void notifyQueue(Transaction transaction) {
        saveNotification(transaction, false);
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void notificationSchedule() {
        logger.debug(messageService.getMessage("notify.message.schedule"));

        List<Notification> notificationList = notificationRepository.findListBySent(false);

        if(notificationList == null || notificationList.isEmpty()){
            return;
        }

        notificationList.forEach(this::setNotificationSended);
    }

    private void setNotificationSended(Notification notification) {
        notification.setSent(true);
        save(notification);
        logger.debug(messageService.getMessage("notify.message.success"));
    }
}
