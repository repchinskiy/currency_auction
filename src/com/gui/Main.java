package com.gui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import com.example.currency_auction.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Main extends TabActivity {
    protected NumberFormat formatter = new DecimalFormat("#0.00");
    DecimalFormat formatter2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();

    {
        customSymbol.setGroupingSeparator(' ');
        formatter2.setDecimalFormatSymbols(customSymbol);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        DataService.getInstance().setMainActivity(this);


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

    public void updateBuyStatistic() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAvgBuyPrice();
                updateBuyRequestCount();
                updateBuySum();
            }
        });
    }

    public void updateSellStatistic() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAvgSellPrice();
                updateSellRequestCount();
                updateSellSum();
            }
        });
    }

    private void updateAvgBuyPrice() {
        TextView avgBuyText = (TextView) findViewById(R.id.avgBuyPrice);
        StringBuilder info = new StringBuilder(formatter.format(DataService.getInstance().getAvgBuy())).append(" грн");
        avgBuyText.setText(info);
    }

    private void updateAvgSellPrice() {
        TextView avgSellText = (TextView) findViewById(R.id.avgSellPrice);
        StringBuilder info = new StringBuilder(formatter.format(DataService.getInstance().getAvgSell())).append(" грн");
        avgSellText.setText(info);
    }

    private void updateBuyRequestCount() {
        TextView buyRequestCount = (TextView) findViewById(R.id.buyRequestCount);
        StringBuilder info = new StringBuilder(String.valueOf(DataService.getInstance().getBuyRequestCount())).append("/");
        buyRequestCount.setText(info);
    }

    private void updateSellRequestCount() {
        TextView sellRequestCount = (TextView) findViewById(R.id.sellRequestCount);
        sellRequestCount.setText(String.valueOf(DataService.getInstance().getSellRequestCount()));
    }

    private void updateBuySum() {
        TextView buySum = (TextView) findViewById(R.id.buySum);
        StringBuilder info = new StringBuilder(formatter2.format((int) DataService.getInstance().getSumBuy())).append(" $");
        buySum.setText(info);
    }

    private void updateSellSum() {
        TextView sellSum = (TextView) findViewById(R.id.sellSum);
        StringBuilder info = new StringBuilder(formatter2.format((int) DataService.getInstance().getSumSell())).append(" $");
        sellSum.setText(info);
    }

//    Средняя покупка:25,90 грн  avgBuyPrice
//    Средняя продажа:26,30 грн  avgSellPrice
//
//    На покупку 37 заявок     buyRequestCount
//    На продажу 42 заявки      sellRequestCount
//    Покупают   367 500 $        buySum
//    Продают    129 250 $       sellSum
}
