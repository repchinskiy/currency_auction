package com.gui;

import com.gui.list.ListHandler;
import com.web.bean.CurrencyInfo;

public class BuyActivity extends CurrencyAuctionActivity {
    protected void createMoreCurrencyInfoItems(ListHandler<CurrencyInfo> handler){
        DataService.getInstance().createMoreBuyCurrencyInfoItems(handler);
    };

}