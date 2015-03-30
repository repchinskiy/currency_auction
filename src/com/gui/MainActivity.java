package com.gui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.example.currency_auction.R;
import com.gui.adapter.TabsPagerAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    protected NumberFormat formatter = new DecimalFormat("#0.00");
    DecimalFormat formatter2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();

    {
        customSymbol.setGroupingSeparator(' ');
        formatter2.setDecimalFormatSymbols(customSymbol);
    }

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private int[] tabsTitleIDs = {R.string.buy_title, R.string.sell_title, R.string.graphic_title};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DataService.getInstance(this);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);

        if (android.os.Build.VERSION.SDK_INT >= 14) {
            actionBar.setHomeButtonEnabled(false);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (int id : tabsTitleIDs) {
            actionBar.addTab(actionBar.newTab().setText(getString(id)).setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        initRegionDialog();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        System.out.println("BIZON MainActivity.onTabReselected");
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        int position = tab.getPosition();
        viewPager.setCurrentItem(position);
        Fragment fragment = mAdapter.getFragment(position);
        if (fragment instanceof IFragmentListener) {
            ((IFragmentListener) fragment).notifySelected();
        }

        System.out.println("BIZON MainActivity.onTabSelected");
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        System.out.println("BIZON MainActivity.onTabUnselected");
    }


    private void initRegionDialog() {
        // адаптер
        String[] data = {"Днепропетровск", "Киев", "Харьков", "Запорожье", "Львов"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.select_region_dialog);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Выберите регион");
        // выделяем элемент
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
//                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
