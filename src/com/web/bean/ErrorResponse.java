package com.web.bean;


public class ErrorResponse implements IResponse {
    private ErrorType errorType = ErrorType.UNRECOGNIZED;
    protected String message;

    public String getMessage() {
        return message != null && message.length() > 0 ? message : "EMPTY_ERROR_MESSAGE";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorType=" + errorType +
                ", message='" + message + '\'' +
                '}';
    }
}