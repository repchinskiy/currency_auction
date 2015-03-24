package com.gui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.currency_auction.R;
import com.web.bean.CurrencyInfo;

/**
 * User: bogdan
 * Date: 18.11.2014
 * Time: 11:47
 */
public class CurrencyInfoAdapter extends ListHandler<CurrencyInfo> {
    private static boolean[] ORDER_ASC_PROPS = {true, false, false}; // 0 - time, 1 - price, 2 - sum

    private class ViewHolder {
        TextView time;
        TextView price;
        TextView sum;
        TextView text;
    }

    public CurrencyInfoAdapter(ListItemsFactory<CurrencyInfo> factory) {
        super(factory);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CurrencyInfo info = items.get(position);
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.currency_info_item, parent, false);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.sum = (TextView) convertView.findViewById(R.id.sum);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            holder.time.setText(info.getTime());
            holder.price.setText(info.getPrice());
            holder.sum.setText(info.getSum());
            holder.text.setText(info.getText());
        }

//        final View row = convertView;

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                factory.setOnClickListener(info, row);
//            }
//        });
        return convertView;
    }

    public boolean isOrderAscTime() {
        return ORDER_ASC_PROPS[0];
    }

    public boolean isOrderAscPrice() {
        return ORDER_ASC_PROPS[1];
    }

    public boolean isOrderAscSum() {
        return ORDER_ASC_PROPS[2];
    }

    public void inverseOrderTime() {
        ORDER_ASC_PROPS[0] = !ORDER_ASC_PROPS[0];
    }

    public void inverseOrderPrice() {
        ORDER_ASC_PROPS[1] = !ORDER_ASC_PROPS[1];
    }

    public void inverseOrderSum() {
        ORDER_ASC_PROPS[2] = !ORDER_ASC_PROPS[2];
    }
}

