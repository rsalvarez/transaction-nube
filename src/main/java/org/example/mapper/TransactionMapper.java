package org.example.mapper;


import org.example.model.request.RequestTransactionDTO;
import org.example.model.transaction.TransactionDTO;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // Indica a MapStruct que genere esto como un Bean de Spring
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDTO(RequestTransactionDTO request);
    RequestTransactionDTO toRequestDTO(TransactionDTO transaction);
}