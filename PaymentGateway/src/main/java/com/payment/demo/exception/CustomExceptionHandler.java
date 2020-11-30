package com.payment.demo.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorDetails> handleException(BindException ex) {

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<ErrorDetails> errorDetails = new ArrayList<>();
        for (FieldError field : errors) {
            ErrorDetails error = new ErrorDetails();
            error.setFieldName(field.getField());
            error.setMessage(field.getDefaultMessage());
            errorDetails.add(error);
        }

        List<ErrorDetails> err = new ArrayList<ErrorDetails>();
        err.addAll(errorDetails);

        return err;
    }

}