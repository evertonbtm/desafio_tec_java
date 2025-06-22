package br.com.batista.desafio01.service.notification;

import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.UnavailableException;
import br.com.batista.desafio01.model.dto.AuthorizeDTO;
import br.com.batista.desafio01.model.dto.NotifyDTO;
import br.com.batista.desafio01.model.entities.Notification;
import br.com.batista.desafio01.model.entities.Transaction;
import br.com.batista.desafio01.repository.INotificationRepository;
import br.com.batista.desafio01.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private INotificationRepository notificationRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Value("${notify.api.url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
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

    @Test
    public void when_CallNotificationApi_then_ReturnNotifyDTO() {
        AuthorizeDTO authorizeDTO = new AuthorizeDTO();
        authorizeDTO.setStatus("success");

        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setStatus("success");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/posts")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(authorizeDTO)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NotifyDTO.class)).thenReturn(Mono.just(notifyDTO));

        Mono<NotifyDTO> result = notificationService.callNotificationApi(authorizeDTO);

        assertNotNull(result);
        assertEquals("success", result.block().getStatus());
    }

    @Test
    public void when_NotificationSchedule_then_ProcessUnsentNotifications() throws Exception {
        Notification unsentNotification = new Notification();
        unsentNotification.setSent(false);

        when(notificationRepository.findListBySent(false)).thenReturn(List.of(unsentNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(unsentNotification);

        notificationService.notificationSchedule();

        verify(notificationRepository, times(1)).save(unsentNotification);
        assertTrue(unsentNotification.isSent());
    }

    @Test
    public void when_NotificationSchedule_WithNoUnsentNotifications_then_DoNothing() throws Exception {
        when(notificationRepository.findListBySent(false)).thenReturn(List.of());

        notificationService.notificationSchedule();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

}
