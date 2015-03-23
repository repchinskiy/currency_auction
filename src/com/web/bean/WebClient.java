package com.web.bean;

import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.web.bean.logger.*;

public class WebClient implements Runnable {
    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static final String GET_BUY = "http://parsers.fxweb.net.ua/api/buy";
    public static final String GET_SELL = "http://parsers.fxweb.net.ua/api/sell";

    protected DefaultHttpClient httpClient = new DefaultHttpClient();
    protected ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    protected final List<WebRequest> webRequests = new LinkedList<WebRequest>();
    protected boolean alive = false;
    protected Thread thread;
    protected boolean logging = true;

    protected long lastSentRequest = 0;
    protected boolean keepAlive = false;

    protected byte[] arr = new byte[4096];

    protected static final String GZIP = "gzip";

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    public WebClient() {
        // The time it takes to open TCP connection.
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
//
//        // Timeout when server does not send data.
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

        // Some tuning that is not required for bit tests.
        httpClient.getParams().setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        httpClient.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
        httpClient.getParams().setParameter("http.protocol.expect-continue", false);

        //remove User-Agent: Apache-HttpClient/UNAVAILABLE (java 1.4) from header
        httpClient.getParams().removeParameter("http.useragent");

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                // Inflate any responses compressed with gzip
                final HttpEntity entity = response.getEntity();
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equals(GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });

        alive = true;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void unSecure() {
        for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
            if (cookie.isSecure() && cookie instanceof BasicClientCookie) {
                ((BasicClientCookie) cookie).setSecure(false);
            }
        }
    }


    public void stop() {
        alive = false;
        thread.interrupt();
    }

    @Override
    public void run() {
        try {
            while (alive) {
                synchronized (webRequests) {
                    webRequests.wait(1000);
                }

                while (alive && !webRequests.isEmpty()) {

                    WebRequest webRequest;
                    synchronized (webRequests) {
                        webRequest = webRequests.remove(0);
                    }

                    send(webRequest);
                }

            }
        } catch (InterruptedException e) {
            if (logging) {
//                Logger.error("Loop interrupted", e, false);
            }
        }
    }

    public void request(WebRequest webRequest) {
        synchronized (webRequests) {
            webRequests.add(webRequest);
            webRequests.notifyAll();
        }
    }

    protected void send(WebRequest webRequest) {
//        Logger.trace("REQUEST " + webRequest.url);

        lastSentRequest = System.currentTimeMillis();
        Object response = null;
        try {
            HttpPost httpPost = new HttpPost(webRequest.url);

//            //upload file
//            if (webRequest.file != null) {
//                File file = new File(webRequest.file);
//                byte fileContent[] = new byte[(int) file.length()];
//                FileInputStream fileInputStream = new FileInputStream(file);
//                fileInputStream.read(fileContent);
//
//                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                entity.addPart("data", new ByteArrayBody(fileContent, "application/octet-stream", file.getName()));
//                httpPost.setEntity(entity);
//            }

            if (webRequest.request != null) { //если есть json запрос
                // если отправляем json
                httpPost.addHeader("Content-Type", "application/json");
                httpPost.addHeader("Accept-Encoding", GZIP);

                try {
                    StringWriter jsonData = new StringWriter();
                    getObjectMapper().writeValue(jsonData, webRequest.request);
                    httpPost.setEntity(new ByteArrayEntity(jsonData.toString().getBytes()));
                } catch (JsonGenerationException ex) {
                    Logger.error("BIZON TankWebClient JsonGenerationException while parse object to json-string for request class: " + webRequest.request.getClass(), ex);
                } catch (JsonMappingException ex) {
                    Logger.error("BIZON TankWebClient JsonMappingException while parse object to json-string for request class: " + webRequest.request.getClass(), ex);
                }

            }

            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (SSLException ex) {
                Logger.error("Error HTTPS off", ex);
//                httpPost.setURI(URI.create(webRequest.url.replaceFirst(PROTOCOL_HTTPS, PROTOCOL_HTTP)));
                httpResponse = httpClient.execute(httpPost);
            }

            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();
            try {
                if (200 == httpResponse.getStatusLine().getStatusCode()) {
                    //если есть поределенный тип ответа
                    if (webRequest.responseClass != null) {
                        long now = System.currentTimeMillis();

//                        System.out.println("BIZON: " + new String(read(inputStream)));

                        try {
                            response = getObjectMapper().readValue(inputStream, webRequest.responseClass);
                        } catch (JsonGenerationException ex) {
                            Logger.error("BIZON TankWebClient JsonGenerationException while parse response to object for response class: " + webRequest.responseClass, ex);
                        } catch (JsonMappingException ex) {
                            Logger.error("BIZON TankWebClient JsonMappingException while parse response to object for response class: " + webRequest.responseClass, ex);
                        }


//                        System.out.println("BIZON webRequest.responseClass : " + webRequest.responseClass + " finish parse: " + (System.currentTimeMillis() - now) + "ms");

                        if (webRequest.listener != null) {
                            Logger.trace("ON RESPONSE " + response);
                            webRequest.listener.onResponse(response);
                        }
                    } else {
                        if (webRequest.listener instanceof WebListenerInputStream) {
                            //просто передаем InputStream
//                            Logger.trace("ON ERROR 2 " + response);
                            ((WebListenerInputStream) webRequest.listener).onResponse(inputStream);
                        } else if (webRequest.listener != null) {
                            //вычитываем всесь ресурс в память
//                            Logger.trace("ON ERROR 3 " + response);
                            webRequest.listener.onResponse(read(inputStream));
                        }
                    }
                } else if (404 == httpResponse.getStatusLine().getStatusCode()) {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setErrorType(ErrorType.RESOURCE_NOT_AVAILABLE);
                    errorResponse.setMessage(new String(read(inputStream)));
                    Logger.trace("```ERROR NOT FOUND " + errorResponse);
                    webRequest.listener.onError(errorResponse);
                } else {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setMessage(new String(read(inputStream)));
//                Logger.trace("ON ERROR 6 " + response);
                    webRequest.listener.onError(errorResponse);
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    if (logging) {
                        Logger.error("Error closing input stream", ex, false);
                    }
                }
                entity.consumeContent();
            }

        } catch (SocketTimeoutException ex) {
            if (logging) {
                Logger.error("Socket timeout exception " + webRequest.url + " " + ex.getLocalizedMessage(), ex, false);
            }
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("socket timeout exception");
//            Logger.trace("ON ERROR 7 " + errorResponse.getMessage());
            webRequest.listener.onError(errorResponse);
        } catch (Exception ex) {
            if (logging) {
                Logger.error("Error sending " + webRequest.url, ex, false);
            }
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(ex.toString());
            if (ex instanceof ConnectException || ex instanceof ConnectTimeoutException) {
                errorResponse.setErrorType(ErrorType.CONNECTION_REFUSED);
            }
//            Logger.trace("ON ERROR 8 " + errorResponse.getMessage());
            webRequest.listener.onError(errorResponse);
        }
    }


    protected byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = is.read(arr)) > 0) {
            byteArrayOutputStream.write(arr, 0, read);
        }
        byte[] result = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return result;
    }

    public void enableLogging(boolean logging) {
        this.logging = logging;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
