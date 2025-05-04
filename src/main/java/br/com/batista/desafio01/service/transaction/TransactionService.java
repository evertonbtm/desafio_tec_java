package br.com.batista.desafio01.service.transaction;

import br.com.batista.desafio01.exception.*;
import br.com.batista.desafio01.model.dto.TransactionDTO;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.entities.Transaction;
import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.repository.ITransactionRepository;
import br.com.batista.desafio01.service.notification.INotificationService;
import br.com.batista.desafio01.service.user.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;

    private final IUserService userService;

    private final INotificationService notificationService;

    public TransactionService(ITransactionRepository transactionRepository, IUserService userService, INotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public TransactionDTO toDTO(Transaction entity){
        return entity == null ? null : new TransactionDTO(entity);
    }

    @Override
    public List<TransactionDTO> toDTO(List<Transaction> entityList){
        return entityList.stream().map(TransactionDTO::new).toList();
    }

    @Override
    public Transaction toEntity(TransactionDTO dto){
        return toEntity(new Transaction(), dto);
    }

    @Override
    public Transaction toEntity(Transaction transaction, TransactionDTO dto){

        transaction.setValue(dto.getValue());
        transaction.setCreateDate(dto.getCreateDate());
        transaction.setMovimentDate(dto.getMovimentDate());

        return transaction;
    }

    @Transactional
    @Override
    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByUser(String userDocumentOrEmail) {
        List<Transaction> transactionList = transactionRepository.findListByUser(userDocumentOrEmail);

        if(transactionList == null || transactionList.isEmpty()){
            return null;
        }

        return transactionList.get(0);
    }

    @Override
    public Transaction processDTO(TransactionDTO transactionDTO) {

        var transaction = new Transaction();

        transaction.setValue(transactionDTO.getValue());
        transaction.setCreateDate((transactionDTO.getCreateDate() == null ? new Date() : transactionDTO.getCreateDate()));
        transaction.setMovimentDate((transactionDTO.getMovimentDate() == null ? new Date() : transactionDTO.getMovimentDate()));

        validatePayerUser(transaction, transactionDTO);
        validatePayeeUser(transaction, transactionDTO);

        validatePayerBalance(transaction);

        Transaction response = save(transaction);

        calculatePayerBalance(transaction);
        calculatePayeeBalance(transaction);

        notificationService.notify(response);

        return response;
    }

    private void validatePayerUser(Transaction transaction, TransactionDTO transactionDTO) throws FieldDuplicatedException, UserDeletedException  {
        var payer = userService.findByDocumentOrEmail(transactionDTO.getPayer(), transactionDTO.getPayer());

        if(payer == null){
            throw new UserNotFoundException(User.class, "document or email", transactionDTO.getPayer());
        }

        transaction.setPayer(payer);
    }

    private void validatePayeeUser(Transaction transaction, TransactionDTO transactionDTO) throws FieldDuplicatedException, UserDeletedException {
        var payer = userService.findByDocumentOrEmail(transactionDTO.getPayee(), transactionDTO.getPayee());

        if(payer == null){
            throw new UserNotFoundException(User.class, "document or email", transactionDTO.getPayer());
        }

        transaction.setPayee(payer);
    }

    private void  validatePayerBalance(Transaction transaction) throws InsuficientBalanceException{
        var payer = transaction.getPayer();
        if(payer.getMoneyBalance().compareTo(transaction.getValue()) < 0){
            throw new InsuficientBalanceException(User.class,"moneyBalance", payer.getMoneyBalance().toPlainString());
        }
    }

    private void calculatePayerBalance(Transaction transaction){
        var payer = transaction.getPayer();

        BigDecimal userBalance = payer.getMoneyBalance();

        BigDecimal newBalance = userBalance.subtract(transaction.getValue());
        payer.setMoneyBalance(newBalance);

        UserDTO userDTO = userService.toDTO(payer);
        userService.update(userDTO);
    }

    private void calculatePayeeBalance(Transaction transaction){
        var payee = transaction.getPayee();

        BigDecimal userBalance = payee.getMoneyBalance();

        BigDecimal newBalance = userBalance.add(transaction.getValue());
        payee.setMoneyBalance(newBalance);

        UserDTO userDTO = userService.toDTO(payee);
        userService.update(userDTO);
    }

}
