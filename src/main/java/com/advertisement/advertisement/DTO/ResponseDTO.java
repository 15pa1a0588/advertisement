package com.advertisement.advertisement.DTO;

import java.io.Serializable;
import java.util.HashMap;

public class ResponseDTO<T> implements Serializable{

    private String status;
    private String message;
    private T response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}



