package org.example.model.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.model.pays.PayMethod;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String id;
    private BigDecimal value;
    private String description;
    private PayMethod method;
    private String cardNumber;
    private String cardHolderName;
    private String cardExpirationDate;
    private String cvv;
}