package com.gui;

import android.app.Activity;
import com.gui.list.ListHandler;
import com.service.Utils;
import com.service.WebClientSingleton;
import com.web.bean.*;
import com.web.bean.logger.Logger;

/**
 * Created by Bizon on 24.03.2015.
 */
public class DataService {
    private static DataService instance;
    private WebClient webClient;

    public static DataService getInstance() {
        if (instance == null) {

            instance = new DataService();
        }
        return instance;
    }

    public DataService() {
        webClient = WebClientSingleton.getInstance();
        webClient.setKeepAlive(true);
    }

    public void createMoreCurrencyInfoItems(final Activity activity, final ListHandler handler, final String url) {
        webClient.request(new WebRequest<>(url, new CurrencyRequest(), CurrencyResponse.class, new WebListener<CurrencyResponse>() {

            @Override
            public void onResponse(final CurrencyResponse response) {
                Utils.prepareResponse(response);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.takeMoreItems(response.getCurrencyInfoList().size(), response.getCurrencyInfoList());
                    }
                });
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                Logger.trace("BIZON url: " + url + ", " + errorResponse);
            }
        }));
    }
}
