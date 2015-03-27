package com.gui;

import android.os.Handler;
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
    private final static long UPDATE_INTERVAL = 60_000;
    private final static long REQUEST_ATTEMPT_INTERVAL = 3000;

    private static DataService instance;
    private WebClient webClient;
    private List<CurrencyInfo> buyCurrencyInfoList = new ArrayList<>();
    private List<CurrencyInfo> sellCurrencyInfoList = new ArrayList<>();

    private long lastUpdateBuy = 0;
    private long lastUpdateSell = 0;
    private MainActivity mainActivityActivity;
    private Handler handler = new Handler();

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public static DataService getInstance(MainActivity mainActivity) {
        instance = getInstance();
        instance.setMainActivityActivity(mainActivity);
        instance.updateData();

        return instance;
    }


    public DataService() {
        webClient = WebClientSingleton.getInstance();
        webClient.setKeepAlive(true);
    }

    public void setMainActivityActivity(MainActivity mainActivityActivity) {
        this.mainActivityActivity = mainActivityActivity;
    }

    public void updateData() {
        updateBuy();
        updateSell();
    }

    public synchronized void updateBuy() {
        updateBuy(null);
    }

    public synchronized void updateBuy(final ListHandler listHandler) {
        System.out.println("BIZON DataService.updateBuy request");
        getData(WebClient.GET_BUY, new ResponseNotifier() {
            @Override
            public void notify(IResponse response) {
                if (response instanceof CurrencyResponse) {
                    CurrencyResponse currencyResponse = (CurrencyResponse) response;
                    buyCurrencyInfoList = currencyResponse.getCurrencyInfoList();
                    lastUpdateBuy = System.currentTimeMillis();
                    mainActivityActivity.updateBuyStatistic();
                    if (listHandler != null) {
                        createMoreBuyCurrencyInfoItems(listHandler);
                    }
                    System.out.println("BIZON DataService.updateBuy response");
                } else {
//                    // через REQUEST_ATTEMPT_INTERVAL мс пробуем еще раз
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateBuy(listHandler);
                        }
                    }, REQUEST_ATTEMPT_INTERVAL);
                }
            }
        });
    }

    public synchronized void updateSell() {
        updateSell(null);
    }

    public synchronized void updateSell(final ListHandler listHandler) {
        System.out.println("BIZON DataService.updateSell request");
        getData(WebClient.GET_SELL, new ResponseNotifier() {
            @Override
            public void notify(IResponse response) {
                if (response instanceof CurrencyResponse) {
                    CurrencyResponse currencyResponse = (CurrencyResponse) response;
                    sellCurrencyInfoList = currencyResponse.getCurrencyInfoList();
                    lastUpdateSell = System.currentTimeMillis();
                    mainActivityActivity.updateSellStatistic();
                    if (listHandler != null) {
                        createMoreSellCurrencyInfoItems(listHandler);
                    }
                    System.out.println("BIZON DataService.updateSell response");
                } else {
//                    // через REQUEST_ATTEMPT_INTERVAL мс пробуем еще раз
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateSell(listHandler);
                        }
                    }, REQUEST_ATTEMPT_INTERVAL);
                }
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

    public void createMoreBuyCurrencyInfoItems(final ListHandler handler) {
        System.out.println("BIZON DataService.createMoreBuyCurrencyInfoItems");
        if (System.currentTimeMillis() - lastUpdateBuy > UPDATE_INTERVAL) {
            updateBuy(handler);
            return;
        }

        mainActivityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.removeAllItems();
                handler.takeMoreItems(buyCurrencyInfoList.size(), buyCurrencyInfoList);
            }
        });
    }

    public void createMoreSellCurrencyInfoItems(final ListHandler handler) {
        System.out.println("BIZON DataService.createMoreSellCurrencyInfoItems");
        if (System.currentTimeMillis() - lastUpdateSell > UPDATE_INTERVAL) {
            updateSell(handler);
            return;
        }

        mainActivityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.removeAllItems();
                handler.takeMoreItems(sellCurrencyInfoList.size(), sellCurrencyInfoList);
            }
        });
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
