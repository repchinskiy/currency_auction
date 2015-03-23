package com.web.bean;

import java.io.InputStream;

public class WebListenerInputStream implements WebListener<InputStream> {

    @Override
    public void onResponse(InputStream response) {
    }

    @Override
    public void onError(ErrorResponse errorResponse) {
    }
}
