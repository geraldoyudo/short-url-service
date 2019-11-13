package com.gerald.shorturl.models;

public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse() {
    }

    private ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorResponse error(String code, String message){
        return new ErrorResponse(code, message);
    }
}
