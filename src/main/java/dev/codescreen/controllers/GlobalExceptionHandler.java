package dev.codescreen.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.codescreen.schemas.ServerError;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param MethodArgumentNotValidException 
     * @return ResponseEntity<ServerError>
     * 
     * Function that is a part of the global exception handler, accounting
     * for when a validation error is thrown and formatting it accordingly.
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ServerError> handleException(MethodArgumentNotValidException e)
    {
        ServerError error = new ServerError(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
}
