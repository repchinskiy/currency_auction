package com.gui.list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.example.currency_auction.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ListHandler<I> extends BaseAdapter {
    protected ListView listView;
    protected View waitItem;

    protected int totalCount;
    protected List<I> items;
    protected boolean requestMode;

    protected ListItemsFactory<I> factory;

    protected LayoutInflater inflater;
    protected Activity activity;
    protected int currentPage;
    protected int countPage;

    protected ProgressBar loadingProgress;

    public ListHandler() {
    }

    public ListHandler(ListItemsFactory<I> factory) {
        this.factory = factory;
    }

    public ListItemsFactory<I> getFactory() {
        return factory;
    }

    public void setFactory(ListItemsFactory<I> factory) {
        this.factory = factory;
    }

    public void create(Activity activity, View view, int emptyListStringId) {
        this.activity = activity;
        create(activity, view, emptyListStringId, new ArrayList<I>());
    }

    public void create(Activity activity, View view, int emptyListStringId, List<I> items) {
        this.activity = activity;
        create(view, emptyListStringId, items, true);
    }

    public void create(Activity activity, View view, int emptyListStringId, List<I> items, boolean createMoreItems) {
        this.activity = activity;
        create(view, emptyListStringId, items, createMoreItems);
    }

    public void create(View view, int emptyListStringId, List<I> items, boolean createMoreItems) {
        this.items = items;

        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            listView = (ListView) activity.findViewById(R.id.list);
        } else {
            listView = (ListView) view.findViewById(R.id.list);
        }

        if (items.size() == 0 && createMoreItems) {
            waitItem = inflater.inflate(R.layout.loading, null);
            waitItem.setOnClickListener(null);

            loadingProgress = (ProgressBar) waitItem.findViewById(R.id.progress_anim);
            loadingProgress.setVisibility(View.VISIBLE);

            if (listView.getFooterViewsCount() < 1) {
                listView.addFooterView(waitItem);
            }

        }

        listView.setAdapter(this);

        if (items.size() == 0 && createMoreItems) {
            createMoreItems(0);
        }

        notifyDataSetChanged();
    }

    public void takeMoreItems(int totalCount, List<I> items) {
        listView.removeFooterView(waitItem);

        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }

        if (items.size() == 0) {
            totalCount = this.items.size();
        }

        if (totalCount > 0) {
            this.totalCount = totalCount;
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        requestMode = false;
    }

    public void takeMoreItemsInHead(int totalCount, List<I> items) {
        listView.removeFooterView(waitItem);

        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }

        if (items.size() == 0) {
            totalCount = this.items.size();
        }

        if (totalCount > 0) {
            this.totalCount = totalCount;
            for (I i : items) {
                this.items.add(0, i);
            }

            notifyDataSetChanged();
        }

        requestMode = false;
    }

    public void refreshItems(int totalCount) {
        listView.removeFooterView(waitItem);

        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }

        if (totalCount > 0) {
            this.totalCount = totalCount;
            notifyDataSetChanged();
        }

        requestMode = false;
    }

    public void refresh() {
        createMoreItems(0);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        listView.setOnScrollListener(onScrollListener);
    }

    public ListView getListView() {
        return listView;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<I> getAllItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public I getItem(int position) {
        final int count = items.size();
        if (totalCount > count && position == count - 1 && !requestMode) {
            createMoreItems(count);
        }
        return items.get(position);
    }

    public void createMoreItems(int position) {
//        if (waitItem != null) {
//            listView.addFooterView(waitItem);
//        }
        requestMode = true;
        factory.createMoreItems(this, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void clear() {
        factory = null;
        setOnItemClickListener(null);
        items.clear();
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        if (waitItem != null) {
            listView.removeFooterView(waitItem);
        }

        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public boolean isRequestMode() {
        return requestMode;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void sort(Comparator<I> comparator, Runnable runnable) {
        Collections.sort(items, comparator);

        if (runnable != null) {
            runnable.run();
        }

        notifyDataSetChanged();
    }
}

