package com.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.currency_auction.R;
import com.gui.list.CurrencyInfoAdapter;
import com.gui.list.ListHandler;
import com.gui.list.ListItemsFactory;
import com.web.bean.CurrencyInfo;
import com.web.bean.WebClient;

import java.util.Comparator;

/**
 * Created by Bizon on 24.03.2015.
 */
public class CurrencyAuctionActivity extends Activity {
    protected String url = WebClient.GET_BUY;
    private View main;
    private CurrencyInfoAdapter currencyInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = findViewById(R.layout.main);
        setContentView(R.layout.tab);
    }

    public void initCurrencyInfoAdapter() {
        findViewById(R.id.price_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("BIZON CurrencyAuctionActivity.onClick");
                currencyInfoAdapter.sort(new Comparator<CurrencyInfo>() {
                    @Override
                    public int compare(CurrencyInfo currencyInfo, CurrencyInfo t1) {
                        int factor = 1;
                        if (!currencyInfoAdapter.isOrderAsc()) {
                            factor = -1;
                        }
                        return factor * currencyInfo.getPrice().compareTo(t1.getPrice());
                    }
                });
            }
        });

        currencyInfoAdapter = new CurrencyInfoAdapter(new ListItemsFactory<CurrencyInfo>() {
            @Override
            public void createMoreItems(ListHandler<CurrencyInfo> handler, int position) {
                DataService.getInstance().createMoreCurrencyInfoItems(CurrencyAuctionActivity.this, currencyInfoAdapter, url);
            }

            @Override
            public void setOnClickListener(CurrencyInfo item, View view) {

            }

            @Override
            public void setOnLongClickListener(CurrencyInfo item, View view) {

            }
        });

        currencyInfoAdapter.removeAllItems();
        currencyInfoAdapter.create(this, main, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCurrencyInfoAdapter();
    }
}
