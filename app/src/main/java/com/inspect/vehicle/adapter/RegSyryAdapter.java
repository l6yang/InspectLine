package com.inspect.vehicle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseListAdapter;
import com.inspect.vehicle.base.SybmSyryBean;

import butterknife.BindView;

public class RegSyryAdapter extends BaseListAdapter<SybmSyryBean, RegSyryAdapter.ViewHolder> {

    public RegSyryAdapter(Context context) {
        super(context);
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
        String yhdh = excuteObjList == null ? "" : replaceNull(excuteObjList.getYhdh());
        String xm = excuteObjList == null ? "" : replaceNull(excuteObjList.getXm());
        holder.itemSpinner.setText(String.format("%s:%s", yhdh, xm));
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.item_text_spin)
        TextView itemSpinner;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
