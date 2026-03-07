package br.com.batista.desafio01.service.transaction;

import br.com.batista.desafio01.model.dto.TransactionDTO;
import br.com.batista.desafio01.model.entities.Transaction;
import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.InsuficientBalanceException;
import br.com.batista.desafio01.exception.UnavailableException;
import br.com.batista.desafio01.exception.UserDeletedException;
import br.com.batista.desafio01.exception.UserNotFoundException;

import java.util.List;

public interface ITransactionService {

    TransactionDTO toDTO(Transaction entity);

    List<TransactionDTO> toDTO(List<Transaction> entityList);

    Transaction toEntity(TransactionDTO dto);

    Transaction toEntity(Transaction transaction, TransactionDTO dto);

    Transaction save(Transaction transaction);

    Transaction findByUser(String userDocumentOrEmail) throws FieldDuplicatedException, UserDeletedException;

    Transaction processDTO(TransactionDTO transactionDTO)
            throws FieldDuplicatedException, UserDeletedException, UserNotFoundException,
                   InsuficientBalanceException, UnavailableException;
}
