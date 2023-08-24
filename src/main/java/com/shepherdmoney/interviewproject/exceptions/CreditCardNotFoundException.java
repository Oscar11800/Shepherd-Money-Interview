package com.shepherdmoney.interviewproject.exceptions;

/**
 * Custom exception class to represent the scenario when a credit card is not found.
 */
public class CreditCardNotFoundException extends RuntimeException {

    public CreditCardNotFoundException() {

    }

    public CreditCardNotFoundException(String message) {
        super(message);
    }

    public CreditCardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditCardNotFoundException(Throwable cause) {
        super(cause);
    }

    public CreditCardNotFoundException(String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
