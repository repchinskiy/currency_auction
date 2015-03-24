package com.gui.list;

import android.view.View;

/**
 * Created by IntelliJ IDEA.
 * User: Andrey
 * Date: 08.10.12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public interface ListItemsFactory<I> {

    public void createMoreItems(ListHandler<I> handler, int position);

    public void setOnClickListener(I item, View view);

    public void setOnLongClickListener(I item, View view);
}
