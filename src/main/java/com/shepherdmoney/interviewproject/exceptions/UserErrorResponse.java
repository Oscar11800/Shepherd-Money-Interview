package com.shepherdmoney.interviewproject.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

/*
* Note: while these detailed error responses are informative and helpful
* for development, it may not be feasible for production and may
* be a security concern when users receive it.
* */
public class UserErrorResponse {
    @JsonFormat
    private ZonedDateTime timeStamp;
    private int statusCode;
    private String path;
    private String message;
    public UserErrorResponse(ZonedDateTime timeStamp, int statusCode, String path, String message) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.path = path;
        this.message = message;
    }
}
