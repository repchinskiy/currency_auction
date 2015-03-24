package com.gui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import com.example.currency_auction.R;

public class Main extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, BuyActivity.class);
        spec = tabHost.newTabSpec("buy").setIndicator(getString(R.string.buy_title))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SellActivity.class);
        spec = tabHost.newTabSpec("sell").setIndicator(getString(R.string.sell_title))
                .setContent(intent);
        tabHost.addTab(spec);
    }


}
