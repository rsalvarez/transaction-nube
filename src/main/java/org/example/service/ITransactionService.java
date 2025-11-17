package org.example.service;

import org.example.model.request.RequestTransactionDTO;
import org.example.model.response.ResponseTransaction;


public interface ITransactionService {
    ResponseTransaction addTraansaction(RequestTransactionDTO data);
}
