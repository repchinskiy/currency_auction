package com.gui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.currency_auction.R;
import com.service.WebClientSingleton;
import com.web.bean.*;
import com.web.bean.logger.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main extends Activity {
    private WebClient webClient;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webClient = WebClientSingleton.getInstance();
        webClient.setKeepAlive(true);

        setContentView(R.layout.main);
//        test();
        test2();
    }

    public void test() {

//                webClient.request(new WebRequest<byte[]>(WebClient.GET_BUY, new WebListener<byte[]>()
        webClient.request(new WebRequest<>(WebClient.GET_BUY, new CurrencyRequest(), CurrencyResponse.class, new WebListener<CurrencyResponse>() {

            @Override
            public void onResponse(CurrencyResponse response) {
                System.out.println("BIZON: " + response);
//                String json = null;
//                try {
//                    json = new String(response, "UTF-8");
////                    new String(response, "US-ASCII")
//                    final String responseStr = ;
//                    ;
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }


//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView) findViewById(R.id.mainText);
//                        try {
////                            String resp = URLEncoder.encode(new String(response), HTTP.USER_AGENT);
//
//                            System.out.println("BIZON resp: " + resp);
//                            textView.setText(resp);
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                Logger.trace("BIZON GET_BUY: " + errorResponse);
            }
        }));
    }

    public void test2() {

        CurrencyResponse currencyResponse = new CurrencyResponse();
        List<CurrencyInfo> currencyInfoList = new ArrayList<CurrencyInfo>();
        for (int i = 0; i < 3; i++) {
            CurrencyInfo currencyInfo = new CurrencyInfo();
            currencyInfo.setTime("time" + i);
            currencyInfo.setPrice("price" + i);
            currencyInfo.setSum("sum" + i);
            currencyInfo.setText("text" + i);
            currencyInfoList.add(currencyInfo);
        }
        currencyResponse.setCurrencyInfoList(currencyInfoList);

        try {
            StringWriter jsonData = new StringWriter();
            webClient.getObjectMapper().writeValue(jsonData, currencyResponse);
            System.out.println("BIZON jsonData: " + jsonData);
        } catch (JsonGenerationException ex) {
            Logger.error("BIZON TankWebClient JsonGenerationException while parse object to json-string for request class: ", ex);
        } catch (JsonMappingException ex) {
            Logger.error("BIZON TankWebClient JsonMappingException while parse object to json-string for request class: ", ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
