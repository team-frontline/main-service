package com.frontline.mainservice.controller;

public class CustomResponse {
    private String message;
    private Object data;
    private int status;

    public CustomResponse(String message, Object data, int status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }
    public CustomResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
