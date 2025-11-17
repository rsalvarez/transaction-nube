package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.request.RequestTransactionDTO;
import org.example.model.response.ResponseTransaction;
import org.example.service.ITransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseTransaction> addTransaction(@Valid @RequestBody RequestTransactionDTO data) {
        return ResponseEntity.ok(transactionService.addTraansaction(data));
    }

}
