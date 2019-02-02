package com.inspect.vehicle.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.inspect.vehicle.R;
import com.inspect.vehicle.adapter.LinkAdapter;
import com.inspect.vehicle.base.BaseActivity;
import com.inspect.vehicle.bean.LinkBean;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.gridView)
    GridView gridView;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void afterOnCreate() {
        setActionBack(false);
        gridView.setAdapter(new LinkAdapter(this, "json/link.json", true));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LinkBean linkBean = (LinkBean) parent.getAdapter().getItem(position);
            String packageName = replaceNull(linkBean.getPackName());
            String actUrl = replaceNull(linkBean.getActUrl());
            //packageName = "com.inspect.vehicle";
            //actUrl=".activity.SplashActivity";
            actUrl = actUrl.startsWith(".") ? actUrl.substring(1) : actUrl;
            ComponentName component = new ComponentName(packageName, String.format("%s.%s", packageName, actUrl));
            Intent intentJump = new Intent("android.intent.action.VIEW");
            intentJump.putExtra("idNumber", "513826199707088933");
            intentJump.putExtra("userID", "admin");
            intentJump.putExtra("cylsh", "1170701111111");
            intentJump.putExtra("keystr", "1DSD0EDB00012112");
            intentJump.setComponent(component);
            startActivityForResult(intentJump, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
