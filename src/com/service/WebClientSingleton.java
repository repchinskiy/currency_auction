package com.service;

import com.web.bean.*;

public class WebClientSingleton {
    private static WebClient webClient;

    private WebClientSingleton() {

    }

    public static synchronized WebClient getInstance() {
        if (webClient == null) {
            webClient = new WebClient();
        }

        return webClient;
    }
}
