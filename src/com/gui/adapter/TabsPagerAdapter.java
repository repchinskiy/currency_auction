package com.gui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gui.BuyActivity;
import com.gui.SellActivity;
import com.web.bean.logger.Logger;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private static final Class TABS[] = {BuyActivity.class, SellActivity.class};
//    private static final Fragment FRAGMENTS[] = new Fragment[TABS.length];

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        if (TABS.length > index && index >= 0) {
            try {
                return (Fragment) TABS[index].newInstance();
//                return FRAGMENTS[index] = (Fragment) TABS[index].newInstance();
            } catch (InstantiationException e) {
                Logger.error("Fragment  InstantiationException getItem(int index)", e);
            } catch (IllegalAccessException e) {
                Logger.error("Fragment IllegalAccessException getItem(int index)", e);
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

//    public void notifySelected(int position) {
//        if (TABS.length > position && position >= 0) {
//             FRAGMENTS[position].
//        }
//
//    }
}
