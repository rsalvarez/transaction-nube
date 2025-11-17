package org.example.service.impl;


import org.example.builder.ReceivableBuilder;
import org.example.client.ReceivablesApiClient;
import org.example.client.TransactionApiClient;
import org.example.mapper.TransactionMapper;
import org.example.model.request.RequestTransactionDTO;
import org.example.model.response.ResponseTransaction;
import org.example.model.transaction.TransactionDTO;
import org.example.service.ITransactionService;
import org.example.util.ConstantProcesor;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionApiClient transactionApiClient;
    private final ReceivablesApiClient receivablesApiClient;

    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionApiClient transactionApiClient, ReceivablesApiClient receivablesApiClient, TransactionMapper transactionMapper) {
        this.transactionApiClient = transactionApiClient;
        this.receivablesApiClient = receivablesApiClient;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public ResponseTransaction addTraansaction(RequestTransactionDTO data) {
        ResponseTransaction responseTransaction = new ResponseTransaction();
        TransactionDTO trxDto =  transactionMapper.toDTO(data);
        transactionApiClient.createTransaction(trxDto);
        receivablesApiClient.createReceivable(ReceivableBuilder.buildReceivable(trxDto));
        responseTransaction.setTransactionId(trxDto.getId());
        responseTransaction.setStatus(ConstantProcesor.OK);
        return responseTransaction;
    }

    //@CircuitBreaker(name = "ApiTransactionService", fallbackMethod = "failService")
    private ResponseTransaction generateData(TransactionDTO trxDto) {
        trxDto = transactionApiClient.createTransaction(trxDto);
        receivablesApiClient.createReceivable(ReceivableBuilder.buildReceivable(trxDto));
        ResponseTransaction responseTransaction = new ResponseTransaction();
        responseTransaction.setTransactionId(trxDto.getId());
        responseTransaction.setStatus(ConstantProcesor.OK);
        return  responseTransaction;
    }

    /*public ResponseTransaction failService(TransactionDTO trxDto, ExceptionService ee) {
        ResponseTransaction data = new ResponseTransaction();
        if (ee.getOrigin().equals("ReceivablesApiClient.createReceivable")) {
            data.setTransactionId(null);
            data.setMessage("Service Receivable failed : " + ee.getMessage());
            data.setStatus(ConstantProcesor.FAILED);
        } else if  (ee.getOrigin().equals( "TransactionApiClient.createTransaction")) {
            data.setTransactionId(null);
            data.setMessage("Service Transaction failed : " + ee.getMessage());
            data.setStatus(ConstantProcesor.FAILED);
        }
        return data;
    }*/




}
