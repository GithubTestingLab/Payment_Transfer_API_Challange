package com.dws.challenge.exception;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends BusinessException {
    public InsufficientFundsException(String message , String errorCode) {
        super(message,errorCode);
    }
}
