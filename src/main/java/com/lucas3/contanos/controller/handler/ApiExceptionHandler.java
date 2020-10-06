package com.lucas3.contanos.controller.handler;

import com.lucas3.contanos.model.exception.ReportNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ReportNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(ReportNotFoundException e) {

        return new ResponseEntity<>(new ReportNotFoundResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
