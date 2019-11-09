package com.ts.controller;

import com.ts.model.ErrorResponse;
import com.ts.model.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.UUID;

@ControllerAdvice
public class RestExceptionHandler {
    Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({IllegalArgumentException.class, ResourceNotFoundException.class, Exception.class})
    ResponseEntity<ErrorResponse> handleException(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return getExceptionResponseEntityWithStatus(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ResourceNotFoundException) {
            return getExceptionResponseEntityWithStatus(ex, HttpStatus.NOT_FOUND);
        }
        return getExceptionResponseEntityWithStatus(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ResponseEntity<ErrorResponse> getExceptionResponseEntityWithStatus(Throwable ex, HttpStatus status) {
        String errorCode = UUID.randomUUID().toString();
        logger.error(String.format("Unhandled Exception - Error Code : %s, Error Message : %s", errorCode, ex.getMessage()), ex);
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(errorCode, ex.getMessage()), status);
    }
}
