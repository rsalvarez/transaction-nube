package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.pays.PayMethod;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTransactionDTO {

    @JsonIgnore
    private String id;
    @NotNull(message = "La descripción no puede ser nula o vacía.")
    @JsonProperty("description")
    private String description;
    @NotNull(message = "El importe no puede ser nulo.")
    @JsonProperty("value")
    private BigDecimal value;
    @NotNull(message = "El método de pago no puede ser nulo.")
    private PayMethod method;
    @NotNull(message = "El número de tarjeta no puede ser nulo o vacío.")
    @JsonProperty("cardNumber")
    private String cardNumber;
    //@NotNull(message = "El titular de la tarjeta es obligatorio.")
    @JsonProperty("cardHolderName")
    private String cardHolderName;
    @NotNull(message = "La fecha de expiración no puede ser nula o vacía.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "El formato de expiración debe ser MM/YY (ej. 12/28).")
    @JsonProperty("cardExpirationDate")
    private String cardExpirationDate;
    @NotNull(message = "cvv es obligatorio.")
    @JsonProperty("cvv")
    private String cvv;


}
