package com.web.bean;

public class WebRequest<T> {

    public String url;
    public String file;
    public Object request;
    public Class<T> responseClass;
    public WebListener<T> listener;

    public WebRequest(String url, WebListener<T> listener) {
        this.url = url;
        this.listener = listener;

        checkListener();
    }

    public WebRequest(String url, String file, WebListener<T> listener) {
        this.url = url;
        this.file=file;
        this.listener = listener;

        checkListener();
    }

    public WebRequest(String url, Object request, WebListener<T> listener) {
        this.url = url;
        this.request = request;
        this.listener = listener;

        checkListener();
    }

    public WebRequest(String url, Object request, Class<T> responseClass, WebListener<T> listener) {
        this.url = url;
        this.request = request;
        this.responseClass = responseClass;
        this.listener = listener;

        checkListener();
    }

    /**
     * Заглушка если listener-а нет
     */
    private void checkListener() {
        if (listener == null) {
            listener = new WebListener<T>() {
                @Override
                public void onResponse(Object response) {
                }

                @Override
                public void onError(ErrorResponse errorResponse) {
                }
            };
        }
    }
}
