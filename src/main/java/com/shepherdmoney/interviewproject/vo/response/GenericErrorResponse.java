package com.shepherdmoney.interviewproject.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class GenericErrorResponse {
    @JsonFormat
    private ZonedDateTime timeStamp;
    private int statusCode;
    private String path;
    private String message;
    public GenericErrorResponse(ZonedDateTime timeStamp, int statusCode, String path, String message) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.path = path;
        this.message = message;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
