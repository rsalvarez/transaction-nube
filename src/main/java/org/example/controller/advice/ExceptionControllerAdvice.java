package org.example.controller.advice;

import org.example.exceptions.ExceptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Rafael
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException ex) {
        Object[] arguments = ex.getDetailMessageArguments();
        String argumentsAsString = (arguments != null) ?
                Arrays.stream(arguments)
                        .map(arg -> arg != null || !arg.equals("")  ? arg.toString() : "null")
                        .collect(Collectors.joining(", ")) :
                "";
        return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode().value())).body(argumentsAsString);
    }

    @ExceptionHandler(ExceptionService.class)
    public ResponseEntity<String> handleException(ExceptionService ex) {
        return ResponseEntity.status(HttpStatus.valueOf(400)).body(ex.toString());
    }
}
