package com.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.currency_auction.R;

public class SellActivity extends CurrencyAuctionActivity implements IFragmentListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.sell_content, container, false);
        initCurrencyInfoAdapter();
        return rootView;
    }

    protected void updateData() {
        DataService.getInstance().createMoreSellCurrencyInfoItems(currencyInfoAdapter);
    }

    @Override
    public void notifySelected() {
        updateData();
    }
}