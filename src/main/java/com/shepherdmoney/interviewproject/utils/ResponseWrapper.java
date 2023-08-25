package com.shepherdmoney.interviewproject.utils;

import lombok.Getter;

/**
 * A wrapper class that encapsulates the response for the addCreditCardToUser API.
 * This class is designed to hold either an integer (ID) or a string (error message).
 * It provides a unified response format for the API, allowing it to return either
 * the credit card ID or an error message based on the outcome of the operation.
 */
@Getter
public class ResponseWrapper {
    private Integer id;
    private String message;

    public ResponseWrapper(String message) {
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}