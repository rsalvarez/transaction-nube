package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.model.pays.PayMethod;

@Getter
@Setter
public class RequestTransactionDTO {

    @JsonIgnore
    private String id;
    @NotBlank(message = "La descripción no puede ser nula o vacía.")
    private String description;
    @NotNull(message = "El importe no puede ser nulo.")
    private double value;
    @NotNull(message = "El método de pago no puede ser nulo.")
    private PayMethod method;
    @NotBlank(message = "El número de tarjeta no puede ser nulo o vacío.")
    private String cardNumber;
    @NotBlank(message = "El titular de la tarjeta es obligatorio.")
    private String cardHolderName;
    @NotBlank(message = "La fecha de expiración no puede ser nula o vacía.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "El formato de expiración debe ser MM/YY (ej. 12/28).")
    private String cardExpirationDate;
    @NotBlank(message = "cvv es obligatorio.")
    private String cvv;

}
