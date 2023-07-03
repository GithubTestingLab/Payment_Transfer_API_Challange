package com.dws.challenge.domain;

import lombok.Data;

@Data
public class ErrorResponse {

    private String errorCode;
    private String getErrorMessage;

}
