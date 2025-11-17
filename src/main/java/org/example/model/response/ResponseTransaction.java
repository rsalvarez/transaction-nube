package org.example.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTransaction {

    private String transactionId;
    private String status;

    private String message;

}
