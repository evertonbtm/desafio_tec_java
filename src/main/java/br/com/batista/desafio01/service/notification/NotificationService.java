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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;


@Service
public class NotificationService implements INotificationService {

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private  WebClient webClient;

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private MessageService messageService;

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

    private Mono<NotifyDTO> callNotificationApi(AuthorizeDTO params) {
        return webClient.post()
                .uri("/posts")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(NotifyDTO.class);
    }


    @Override
    public void notify(Transaction transaction) throws Exception {
        NotifyDTO notifyDTO;
        AuthorizeDTO mockParams = new AuthorizeDTO();
        long seconds = Instant.now().getEpochSecond();
        String result = (seconds % 2 == 0) ? "error" : "success";
        mockParams.setStatus(result);

        try {
            // mock api
             notifyDTO = callNotificationApi(mockParams).block();
            if(notifyDTO != null  && notifyDTO.getStatus().equals("success")){
                logger.info(messageService.getMessage("notify.message.success"));
            }else{
                notifyQueue(transaction);
            }
        } catch (Exception e){
            notifyQueue(transaction);
            throw new UnavailableException(Transaction.class);
        }

    }


    public Notification findByTransaction(long idTransation) throws Exception {
        List<Notification> notificationList = notificationRepository.findListByTransactionId(idTransation);

        if(notificationList == null || notificationList.isEmpty()){
            return null;
        }

        if(notificationList.size() > 1){
            throw new FieldDuplicatedException(Notification.class, "transaction", "");
        }

        return notificationList.get(0);
    }

    private void notifyQueue(Transaction transaction) throws Exception {

        Notification notification = findByTransaction(transaction.getIdTransaction());

        if(notification == null){
            notification = new Notification();
            notification.setUserEmail(transaction.getPayee().getEmail());
            notification.setSent(false);
        }

        notification.setTitle(ENotification.TITLE.get());
        notification.setMessage(ENotification.MESSAGE.get()
                    .replace("{0}",String.valueOf(transaction.getValue()))
                    .replace("{1}", transaction.getPayer().getDocument()));

        notification.setTransaction(transaction);

        save(notification);
    }

    @Scheduled(fixedRate = 60000)
    public void notificationSchedule() throws Exception {
        logger.debug(messageService.getMessage("notify.message.schedule"));

        List<Notification> notificationList = notificationRepository.findListBySent(false);

        if(notificationList == null || notificationList.isEmpty()){
            return;
        }

        notificationList.forEach(this::sendNotification);
    }

    private void sendNotification(Notification notification) {
        logger.info(messageService.getMessage("notify.message.success"));
        notification.setSent(true);
        save(notification);
    }
}
