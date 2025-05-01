package br.com.batista.desafio01.controller;

import br.com.batista.desafio01.model.dto.TransactionDTO;
import br.com.batista.desafio01.service.transaction.ITransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class TransactionController {

    private final ITransactionService transactionService;


    public TransactionController(ITransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping(path="send")
    public ResponseEntity<TransactionDTO> send(@Valid @RequestBody TransactionDTO transactionDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransactionDTO(transactionService.processDTO(transactionDTO)));
    }

}
