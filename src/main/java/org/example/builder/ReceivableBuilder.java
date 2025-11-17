package org.example.builder;

import org.example.model.pays.PayMethod;
import org.example.model.receivable.ReceivablesDTO;
import org.example.model.transaction.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceivableBuilder {

    public static ReceivablesDTO buildReceivable(TransactionDTO data) {
        ReceivablesDTO response = new ReceivablesDTO();
        response.setCreateDate(String.valueOf(LocalDate.now()));
        BigDecimal commisionCalc = (commission(data.getMethod()).divide(BigDecimal.valueOf(100)).multiply(data.getValue()));
        BigDecimal total = data.getValue().subtract(commisionCalc);
        response.setDiscount(commission(data.getMethod()));
        response.setTotal(total);
        response.setSubtotal(data.getValue());
        return response;
    }

    private static BigDecimal commission(PayMethod pay) {
        if (pay.equals(PayMethod.credit_card)) return BigDecimal.valueOf(4); else return BigDecimal.valueOf(2);
    }

}
