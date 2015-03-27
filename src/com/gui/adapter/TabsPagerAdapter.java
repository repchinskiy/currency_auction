package com.gui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.gui.BuyActivity;
import com.gui.SellActivity;
import com.web.bean.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private static final Class TABS[] = {BuyActivity.class, SellActivity.class};
    private Map<Integer, Fragment> referenceMap = new HashMap<Integer, Fragment>(TABS.length);

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        if (TABS.length > index && index >= 0) {
            try {
                Fragment fragment = (Fragment) TABS[index].newInstance();
                referenceMap.put(index, fragment);
                return fragment;
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        referenceMap.remove(position);
    }

    public Fragment getFragment(int key) {
        return referenceMap.get(key);
    }
}
