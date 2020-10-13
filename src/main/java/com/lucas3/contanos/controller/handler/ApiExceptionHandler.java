package com.lucas3.contanos.controller.handler;

import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IncidentNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(IncidentNotFoundException e) {

        return new ResponseEntity<>(new IncidentNotFoundResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
