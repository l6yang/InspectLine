package com.inspect.vehicle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseListAdapter;
import com.inspect.vehicle.base.SybmSyryBean;

import java.util.List;

import butterknife.BindView;

public class RegSybmAdapter extends BaseListAdapter<SybmSyryBean, RegSybmAdapter.ViewHolder> {

    public RegSybmAdapter(Context context) {
        super(context);
    }

    public RegSybmAdapter(Context context, List<SybmSyryBean> beanList) {
        super(context, beanList);
    }

    @Override
    public int adapterLayout() {
        return R.layout.item_spinner_list;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onViewHolder(ViewHolder holder, int position) {
        SybmSyryBean excuteObjList = getItem(position);
        holder.itemSpinner.setText(excuteObjList == null ? "" : replaceNull(excuteObjList.getBmjc()));
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.item_text_spin)
        TextView itemSpinner;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
