package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.request.RequestTransactionDTO;
import org.example.model.response.ResponseTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    @PostMapping("/add")
    public ResponseEntity<ResponseTransaction> addTransaction(@Valid RequestTransactionDTO data) {
        return ResponseEntity.ok(null);
    }

}
