package com.gui;

import com.gui.list.ListHandler;
import com.service.Utils;
import com.service.WebClientSingleton;
import com.web.bean.*;
import com.web.bean.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bizon on 24.03.2015.
 */
public class DataService {
    private final static long UPDATE_INTERVAL = 60000;
    private static DataService instance;
    private WebClient webClient;
    private List<CurrencyInfo> buyCurrencyInfoList = new ArrayList<>();
    private List<CurrencyInfo> sellCurrencyInfoList = new ArrayList<>();
    ;
    private boolean updateBuyProcess;
    private boolean updateSellProcess;
    private long lastUpdateBuy = 0;
    private long lastUpdateSell = 0;

    private Main mainActivity;

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public DataService() {
        webClient = WebClientSingleton.getInstance();
        webClient.setKeepAlive(true);
        updateData();
    }

    public void setMainActivity(Main mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void updateData() {
        updateBuy();
        updateSell();
    }

    public void updateBuy() {
        System.out.println("BIZON DataService.updateBuy request");
        updateBuyProcess = true;
        getData(WebClient.GET_BUY, new ResponseNotifier() {
            @Override
            public void notify(IResponse response) {
                if (response instanceof CurrencyResponse) {
                    CurrencyResponse currencyResponse = (CurrencyResponse) response;
                    buyCurrencyInfoList = currencyResponse.getCurrencyInfoList();
                }
                lastUpdateBuy = System.currentTimeMillis();
                updateBuyProcess = false;
                mainActivity.updateBuyStatistic();
                System.out.println("BIZON DataService.updateBuy response");
            }
        });
    }

    public void updateSell() {
        System.out.println("BIZON DataService.updateSell request");
        updateSellProcess = true;
        getData(WebClient.GET_SELL, new ResponseNotifier() {
            @Override
            public void notify(IResponse response) {
                if (response instanceof CurrencyResponse) {
                    CurrencyResponse currencyResponse = (CurrencyResponse) response;
                    sellCurrencyInfoList = currencyResponse.getCurrencyInfoList();
                }
                lastUpdateSell = System.currentTimeMillis();
                updateSellProcess = false;
                mainActivity.updateSellStatistic();
                System.out.println("BIZON DataService.updateSell response");
            }
        });
    }

    private void getData(final String url, final ResponseNotifier responseNotifier) {
        webClient.request(new WebRequest<>(url, new CurrencyRequest(), CurrencyResponse.class, new WebListener<CurrencyResponse>() {
            @Override
            public void onResponse(final CurrencyResponse response) {
                if (responseNotifier != null) {
                    Utils.prepareResponse(response);
                    responseNotifier.notify(response);
                }
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                if (responseNotifier != null) {
                    responseNotifier.notify(errorResponse);
                }
                Logger.trace("BIZON url: " + url + ", " + errorResponse);
            }
        }));
    }


    public void createMoreBuyCurrencyInfoItems(ListHandler handler) {
        System.out.println("BIZON DataService.createMoreBuyCurrencyInfoItems");
        while (updateBuyProcess) {
            // wait
        }

        if (System.currentTimeMillis() - lastUpdateBuy > UPDATE_INTERVAL) {
            updateBuy();
            createMoreBuyCurrencyInfoItems(handler);
            return;
        }

        handler.takeMoreItems(buyCurrencyInfoList.size(), buyCurrencyInfoList);
    }

    public void createMoreSellCurrencyInfoItems(ListHandler handler) {
        System.out.println("BIZON DataService.createMoreSellCurrencyInfoItems");
        while (updateSellProcess) {
            // wait
        }

        if (System.currentTimeMillis() - lastUpdateSell > UPDATE_INTERVAL) {
            updateSell();
            createMoreSellCurrencyInfoItems(handler);
            return;
        }

        handler.takeMoreItems(sellCurrencyInfoList.size(), sellCurrencyInfoList);
    }


    public float getAvgBuy() {
        return getAvgPrice(buyCurrencyInfoList);
    }

    public float getAvgSell() {
        return getAvgPrice(sellCurrencyInfoList);
    }

    public float getSumBuy() {
        return getTotalSum(buyCurrencyInfoList);
    }

    public float getSumSell() {
        return getTotalSum(sellCurrencyInfoList);
    }

    private float getAvgPrice(List<CurrencyInfo> list) {
        float avg = 0;

        if (list != null) {
            int size = list.size();
            return getSumPrice(list) / size;
        }

        return avg;
    }

    private float getSumPrice(List<CurrencyInfo> list) {
        float avg = 0;

        if (list != null) {
            int size = list.size();
            if (size > 0) {
                for (CurrencyInfo currencyInfo : list) {
                    avg += currencyInfo.getPriceF();
                }
                return avg;
            }

        }

        return avg;
    }

    private float getTotalSum(List<CurrencyInfo> list) {
        float avg = 0;

        if (list != null) {
            int size = list.size();
            if (size > 0) {
                for (CurrencyInfo currencyInfo : list) {
                    avg += currencyInfo.getSumF();
                }
                return avg;
            }

        }

        return avg;
    }

    public int getBuyRequestCount() {
        if (buyCurrencyInfoList != null) {
            return buyCurrencyInfoList.size();
        }

        return 0;
    }

    public int getSellRequestCount() {
        if (sellCurrencyInfoList != null) {
            return sellCurrencyInfoList.size();
        }

        return 0;
    }


}
