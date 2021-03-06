package com.gs.buluo.store.view.widget.loadMoreRecycle;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hjn on 2016/11/16.
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        onInitializeView();
    }

    public void onInitializeView() {

    }

    public <Y extends View> Y findViewById(@IdRes int resId) {
        return (Y) itemView.findViewById(resId);
    }

    public void setData(final T entity) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(entity);
            }
        });
    }

    public void onItemViewClick(T entity) {

    }

}
