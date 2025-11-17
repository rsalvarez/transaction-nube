package org.example.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTransaction {

    private String transactionId;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

}
