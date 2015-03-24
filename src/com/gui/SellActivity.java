package com.gui;

import android.os.Bundle;
import com.gui.list.ListHandler;
import com.web.bean.CurrencyInfo;
import com.web.bean.WebClient;

public class SellActivity extends CurrencyAuctionActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void createMoreCurrencyInfoItems(ListHandler<CurrencyInfo> handler){
        DataService.getInstance().createMoreSellCurrencyInfoItems(handler);
    };
}