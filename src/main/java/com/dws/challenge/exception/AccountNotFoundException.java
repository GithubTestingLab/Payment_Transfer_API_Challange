package com.dws.challenge.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class AccountNotFoundException extends BusinessException{

    public AccountNotFoundException(String message , String errorCode){
        super(message , errorCode);
    }

}
