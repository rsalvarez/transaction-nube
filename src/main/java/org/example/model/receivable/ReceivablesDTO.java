package org.example.model.receivable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceivablesDTO {

    private String id;
    private String status;
    private String createDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    @JsonProperty("transaction_id")
    private String transactionId;
}
