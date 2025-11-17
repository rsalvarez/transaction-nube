package org.example.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionService extends RuntimeException{

    private String origin;
    private String urlHost;
    private String path;

    public ExceptionService(String message, String origin, String urlHost, String path) {
        // Llama al constructor de RuntimeException
        super(message);
        this.origin = origin;
        this.urlHost = urlHost;
        this.path = path;
    }




}
