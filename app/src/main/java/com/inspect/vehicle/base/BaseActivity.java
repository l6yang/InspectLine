package com.inspect.vehicle.base;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.inspect.vehicle.impl.IContactsImpl;
import com.inspect.vehicle.impl.ToolBarBackListener;
import com.inspect.vehicle.util.PreferUtil;
import com.loyal.base.ui.activity.ABasicFragActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends ABasicFragActivity implements IContactsImpl {
    private ActionBar actionBar;
    private ToolBarBackListener backListener;

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        setActionBack(true);
    }

    public void hideActionBar(boolean hide) {
        if (hide) {
            if (null != actionBar)
                actionBar.hide();
        }
    }

    public void setActionBack(boolean showHomeAsUp) {
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            actionBar.setHomeButtonEnabled(showHomeAsUp);
        }
    }

    public void setToolbarBackListener(ToolBarBackListener listener) {
        backListener = listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (null != backListener)
                    backListener.onBack();
                else finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getIpAdd() {
        return PreferUtil.getString(getApplicationContext(), StrImpl.BASEURL, StrImpl.ipadd);
    }
}
