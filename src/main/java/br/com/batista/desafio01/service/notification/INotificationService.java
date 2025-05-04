package br.com.batista.desafio01.service.notification;

import br.com.batista.desafio01.exception.UnavailableException;
import br.com.batista.desafio01.model.dto.AuthorizeDTO;
import br.com.batista.desafio01.model.dto.NotifyDTO;
import br.com.batista.desafio01.model.entities.Transaction;
import reactor.core.publisher.Mono;

public interface INotificationService {
    void notify(Transaction transaction) throws UnavailableException;

    Mono<NotifyDTO> callNotificationApi(AuthorizeDTO params);
}
