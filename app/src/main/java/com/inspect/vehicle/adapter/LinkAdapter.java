package com.inspect.vehicle.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseListAdapter;
import com.inspect.vehicle.bean.LinkBean;

import butterknife.BindView;

public class LinkAdapter extends BaseListAdapter<LinkBean, LinkAdapter.ViewHolder> {

    public LinkAdapter(Context context, String json, boolean isFile) {
        super(context, json, LinkBean.class, isFile);
    }

    @Override
    public int adapterLayout() {
        return R.layout.item_link_grid;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onViewHolder(ViewHolder holder, int position) {
        LinkBean linkBean = getItem(position);
        String url = linkBean.getIcon();
        holder.simpleDraweeView.setImageURI(Uri.parse(url));
        String menuText = linkBean.getText();
        holder.textView.setText(menuText);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.item_link_text)
        TextView textView;
        @BindView(R.id.item_link_icon)
        SimpleDraweeView simpleDraweeView;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
