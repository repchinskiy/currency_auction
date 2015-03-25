package com.gui;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import com.example.currency_auction.R;
import com.gui.list.CurrencyInfoAdapter;
import com.gui.list.ListHandler;
import com.gui.list.ListItemsFactory;
import com.web.bean.CurrencyInfo;
import com.web.bean.logger.Logger;

import java.util.Comparator;

/**
 * Created by Bizon on 24.03.2015.
 */
public abstract class CurrencyAuctionActivity extends Fragment {
    private CurrencyInfoAdapter currencyInfoAdapter;
    protected View rootView;

    private void hideAllSortArrow() {
        findViewById(R.id.time_sort_icon).setVisibility(View.INVISIBLE);
        findViewById(R.id.price_sort_icon).setVisibility(View.INVISIBLE);
        findViewById(R.id.sum_sort_icon).setVisibility(View.INVISIBLE);
    }

    private void updateArrow(ImageView arrow, boolean orderAsc) {
        hideAllSortArrow();
        if (orderAsc) {
            arrow.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float));
        } else {
            arrow.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
        }
        arrow.setVisibility(View.VISIBLE);
    }

    private View findViewById(int id) {
       return rootView.findViewById(id);
    }

    protected void initCurrencyInfoAdapter() {
        findViewById(R.id.time_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyInfoAdapter.sort(new Comparator<CurrencyInfo>() {
                    @Override
                    public int compare(CurrencyInfo currencyInfo, CurrencyInfo t1) {
                        int factor = 1;
                        if (!currencyInfoAdapter.isOrderAscTime()) {
                            factor = -1;
                        }
                        return factor * currencyInfo.getTime().compareTo(t1.getTime());
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        currencyInfoAdapter.inverseOrderTime();
                        updateArrow(((ImageView) findViewById(R.id.time_sort_icon)), currencyInfoAdapter.isOrderAscTime());
                    }
                });
            }
        });

        findViewById(R.id.price_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyInfoAdapter.sort(new Comparator<CurrencyInfo>() {
                    @Override
                    public int compare(CurrencyInfo currencyInfo, CurrencyInfo t1) {
                        int factor = 1;
                        if (!currencyInfoAdapter.isOrderAscPrice()) {
                            factor = -1;
                        }
                        return factor * currencyInfo.getPrice().compareTo(t1.getPrice());
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        currencyInfoAdapter.inverseOrderPrice();
                        updateArrow(((ImageView) findViewById(R.id.price_sort_icon)), currencyInfoAdapter.isOrderAscPrice());
                    }
                });
            }
        });

        findViewById(R.id.sum_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyInfoAdapter.sort(new Comparator<CurrencyInfo>() {
                    @Override
                    public int compare(CurrencyInfo currencyInfo, CurrencyInfo t1) {
                        String sum = currencyInfo.getSum().replaceAll("\\$", "").replaceAll("\\s+", "").trim();
                        String sum2 = t1.getSum().replaceAll("\\$", "").replaceAll("\\s+", "").trim();
                        int s = 0, s2 = 0;

                        try {
                            s = Integer.parseInt(sum);
                            s2 = Integer.parseInt(sum2);
                        } catch (NumberFormatException e) {
                            Logger.error("BIZON NumberFormatException can't parse " + sum + " OR " + sum2, e);
                        }

                        int factor = 1;
                        if (!currencyInfoAdapter.isOrderAscSum()) {
                            factor = -1;
                        }
                        return factor * (s - s2);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        currencyInfoAdapter.inverseOrderSum();
                        updateArrow(((ImageView) findViewById(R.id.sum_sort_icon)), currencyInfoAdapter.isOrderAscSum());
                    }
                });
            }
        });

        currencyInfoAdapter = new CurrencyInfoAdapter(new ListItemsFactory<CurrencyInfo>() {
            @Override
            public void createMoreItems(ListHandler<CurrencyInfo> handler, int position) {
                createMoreCurrencyInfoItems(handler);
            }

            @Override
            public void setOnClickListener(CurrencyInfo item, View view) {

            }

            @Override
            public void setOnLongClickListener(CurrencyInfo item, View view) {

            }
        });

        currencyInfoAdapter.removeAllItems();
        currencyInfoAdapter.create(getActivity(), rootView, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        initCurrencyInfoAdapter();
    }

    protected abstract void createMoreCurrencyInfoItems(ListHandler<CurrencyInfo> handler);
}
