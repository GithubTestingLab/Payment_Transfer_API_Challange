package com.dws.challenge.exception;

import com.dws.challenge.domain.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
@ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(HttpServletRequest request, Exception ex){
        BusinessException businessEx = (BusinessException) ex;
        String errorMsg = (ex.getMessage() == null) ? ex.getClass().getSimpleName() : ex.getMessage();
        log.error("[" +businessEx.getErrorCode() + "]" + businessEx.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(businessEx.getErrorCode());
        response.setGetErrorMessage(errorMsg);

        return ResponseEntity.status(businessEx.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest request, Exception ex){

        log.error(ex.getMessage(), ex);
        String errorMsg = (ex.getMessage() == null) ? ex.getClass().getSimpleName() : ex.getMessage();
        Map<String ,Object> error = Collections.singletonMap("error", errorMsg);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
