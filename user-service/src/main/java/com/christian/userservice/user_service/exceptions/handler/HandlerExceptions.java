package com.christian.userservice.user_service.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.christian.userservice.user_service.exceptions.custom.UsernameNotFoundException;
import com.christian.userservice.user_service.exceptions.dto.Error;

import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Error> notFoundEx(Exception e) {
        Error error = new Error();
        error.setMessage("Esta ruta no existe en la api");
        error.setDate(new Date());
        error.setError(e.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Error> usernameNotFoundEx(Exception e) {
        Error error = new Error();
        error.setMessage("El username no existe en la base de datos");
        error.setDate(new Date());
        error.setError(e.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);

    }

}
