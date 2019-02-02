package com.inspect.vehicle.base;

import android.content.Context;
import android.view.View;

import com.loyal.base.adapter.ABasicListAdapter;
import com.loyal.base.adapter.ABasicListViewHolder;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseListAdapter<T, VH extends ABasicListViewHolder> extends ABasicListAdapter<T, VH>     {

    public BaseListAdapter(Context context) {
        super(context);
    }

    public BaseListAdapter(Context context, List<T> arrList) {
        super(context, arrList);
    }

    public BaseListAdapter(Context context, String json, Class<T> t) {
        super(context, json, t, true);
    }

    public BaseListAdapter(Context context, String json, Class<T> t, boolean isFile) {
        super(context, json, t, isFile);
    }

    public class ViewHolder extends ABasicListViewHolder {

        @Override
        public void bindView(View view) {
            ButterKnife.bind(this, view);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }
}
