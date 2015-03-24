package com.gui;

import android.os.Bundle;
import com.web.bean.WebClient;

public class SellActivity extends CurrencyAuctionActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = WebClient.GET_SELL;
    }
}