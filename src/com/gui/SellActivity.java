package com.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.currency_auction.R;
import com.gui.list.ListHandler;
import com.web.bean.CurrencyInfo;

public class SellActivity extends CurrencyAuctionActivity {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.sell_content, container, false);
        return rootView;
    }

    protected void createMoreCurrencyInfoItems(ListHandler<CurrencyInfo> handler) {
        DataService.getInstance().createMoreSellCurrencyInfoItems(handler);
    }
}