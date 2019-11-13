package com.gerald.shorturl.controllers;

import com.gerald.shorturl.exception.UrlKeyNotFoundException;
import com.gerald.shorturl.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GeneralControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(GeneralControllerAdvice.class);

    @ExceptionHandler(UrlKeyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException() {
        return ErrorResponse.error("10", "Key not found");
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest() {
        return ErrorResponse.error("20", "Bad Request");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Exception ex) {
        log.error("An Unexpected error has occured", ex);
        return ErrorResponse.error("99", "An unexpected error occurred. Please contact the admin.");
    }
}
