package com.alliance.leadbooster.web.advice;

import com.alliance.leadbooster.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    private static final String VALIDATION_ERROR = "validation.error";
    private static final String GENERAL_ERROR = "unknown.error";

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), VALIDATION_ERROR, null);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), VALIDATION_ERROR, null);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        ApiError apiError = new ApiError("Oops, something bad happened", GENERAL_ERROR, null);
        log.error("Unhandled Exception occurred", ex);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
