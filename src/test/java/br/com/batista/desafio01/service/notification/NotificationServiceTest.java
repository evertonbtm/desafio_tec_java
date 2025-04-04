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
import br.com.batista.desafio01.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private INotificationRepository notificationRepository;

    @Mock
    private MessageService messageService;

    @MockitoBean
    private WebClient webClient;

    @MockitoBean
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @MockitoBean
    private WebClient.RequestBodySpec requestBodySpec;

    @MockitoBean
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @MockitoBean
    private WebClient.ResponseSpec responseSpec;

    @Value("${notify.api.url}")
    private String apiUrl;

    @BeforeEach
    public void setUp() {
        notificationService.init();
    }

    @Test
    public void when_SaveNotification_then_success() {
        Notification notification = new Notification();
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification savedNotification = notificationService.save(notification);

        assertNotNull(savedNotification);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    public void when_notify_then_UnavailableException() throws Exception {
        Transaction transaction = MockUtils.mockTransaction();
        AuthorizeDTO authorizeDTO = new AuthorizeDTO();
        authorizeDTO.setStatus("error");

        assertThrows(UnavailableException.class, () -> notificationService.notify(transaction));
    }

    @Test
    public void when_FindByTransaction_then_success() throws Exception {
        Notification notification = new Notification();
        List<Notification> notifications = List.of(notification);

        when(notificationRepository.findListByTransactionId(anyLong())).thenReturn(notifications);

        Notification foundNotification = notificationService.findByTransaction(1L);

        assertNotNull(foundNotification);
        verify(notificationRepository, times(1)).findListByTransactionId(1L);
    }

    @Test
    public void when_FindByTransaction_then_ThrowsFieldDuplicatedException() {
        List<Notification> notifications = List.of(new Notification(), new Notification());

        when(notificationRepository.findListByTransactionId(anyLong())).thenReturn(notifications);

        assertThrows(FieldDuplicatedException.class, () -> notificationService.findByTransaction(1L));
    }


}
