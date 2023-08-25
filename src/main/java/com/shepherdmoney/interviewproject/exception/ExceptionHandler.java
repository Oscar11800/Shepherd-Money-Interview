package com.shepherdmoney.interviewproject.exception;

import com.shepherdmoney.interviewproject.vo.response.CreditCardErrorResponse;
import com.shepherdmoney.interviewproject.vo.response.GenericErrorResponse;
import com.shepherdmoney.interviewproject.vo.response.UserErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.util.Date;


@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException exception,
                                                                         HttpServletRequest req) {
        UserErrorResponse error = new UserErrorResponse(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                req.getRequestURI(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<CreditCardErrorResponse> handleCreditCardNotFoundException(CreditCardNotFoundException exception,
                                                                                     HttpServletRequest req) {
        CreditCardErrorResponse error = new CreditCardErrorResponse(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                req.getRequestURI(),
                exception.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<Object> handleGenericException(Exception exception, HttpServletRequest req){
        GenericErrorResponse error = new GenericErrorResponse(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                req.getRequestURI(),
                exception.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
