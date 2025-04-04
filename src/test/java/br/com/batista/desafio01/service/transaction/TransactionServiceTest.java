package br.com.batista.desafio01.service.transaction;



import br.com.batista.desafio01.exception.InsuficientBalanceException;
import br.com.batista.desafio01.exception.UserNotFoundException;
import br.com.batista.desafio01.model.dto.TransactionDTO;
import br.com.batista.desafio01.model.entities.Transaction;
import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.repository.ITransactionRepository;

import br.com.batista.desafio01.service.notification.INotificationService;
import br.com.batista.desafio01.service.user.IUserService;
import br.com.batista.desafio01.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    @Mock
    IUserService userService;

    @Mock
    ITransactionRepository transactionRepository;

    @Mock
    INotificationService notificationService;

    @BeforeEach
    public void init(){
        Mockito.mockitoSession().initMocks(this);
    }

    @Test
    public void when_create_transaction_then_success() throws Exception {

        User payer = MockUtils.mockUser();
        User payee = MockUtils.mockUser();

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setValue(BigDecimal.TEN);
        transactionDTO.setPayee(payee.getDocument());
        transactionDTO.setPayer(payer.getDocument());

        Transaction transaction = transactionService.toEntity(transactionDTO);

        when(userService.findByDocumentOrEmail(transactionDTO.getPayer(), transactionDTO.getPayer())).thenReturn(payer);
        when(userService.findByDocumentOrEmail(transactionDTO.getPayee(), transactionDTO.getPayee())).thenReturn(payee);
        when(transactionService.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction found = transactionService.processDTO(transactionDTO);

        assertEquals(found.getIdTransaction(), transaction.getIdTransaction());
    }

    @Test
    public void when_create_transaction_then_InsuficientBalanceException() throws Exception {

        User payer = MockUtils.mockUser();
        payer.setMoneyBalance(BigDecimal.ZERO);
        User payee = MockUtils.mockUser();

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setValue(BigDecimal.TEN);
        transactionDTO.setPayee(payee.getDocument());
        transactionDTO.setPayer(payer.getDocument());

        when(userService.findByDocumentOrEmail(transactionDTO.getPayer(), transactionDTO.getPayer())).thenReturn(payer);
        when(userService.findByDocumentOrEmail(transactionDTO.getPayee(), transactionDTO.getPayee())).thenReturn(payee);

        assertThrows(InsuficientBalanceException.class, () -> transactionService.processDTO(transactionDTO));
    }

    @Test
    public void when_create_transaction_with_invalid_payer_then_fail() throws Exception {

        User payer = null;
        User payee = MockUtils.mockUser();

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setValue(BigDecimal.TEN);
        transactionDTO.setPayee(payee.getDocument());
        transactionDTO.setPayer("invalid_payer_document");

        when(userService.findByDocumentOrEmail(transactionDTO.getPayer(), transactionDTO.getPayer())).thenReturn(payer);

        assertThrows(UserNotFoundException.class, () -> transactionService.processDTO(transactionDTO));
    }

    @Test
    public void when_create_transaction_with_invalid_payee_then_fail() throws Exception {

        User payer = MockUtils.mockUser();
        User payee = null;

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setValue(BigDecimal.TEN);
        transactionDTO.setPayee("invalid_payee_document");
        transactionDTO.setPayer(payer.getDocument());

        when(userService.findByDocumentOrEmail(transactionDTO.getPayer(), transactionDTO.getPayer())).thenReturn(payer);
        when(userService.findByDocumentOrEmail(transactionDTO.getPayee(), transactionDTO.getPayee())).thenReturn(payee);

        assertThrows(UserNotFoundException.class, () -> transactionService.processDTO(transactionDTO));
    }

    @Test
    public void when_create_transaction_with_null_transactionDTO_then_fail() {
        assertThrows(NullPointerException.class, () -> transactionService.processDTO(null));
    }

}
