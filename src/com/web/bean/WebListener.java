package com.web.bean;


public interface WebListener<T> {

    void onResponse(T response);

    void onError(ErrorResponse errorResponse);
}
