package com.web.bean;

public enum ErrorType {
    UNRECOGNIZED, NO_SESSION, INTERNAL_ERROR, RESOURCE_NOT_AVAILABLE, CONNECTION_REFUSED;

    public ErrorType valueOf(int ordinal) {
        return values()[ordinal];
    }
}