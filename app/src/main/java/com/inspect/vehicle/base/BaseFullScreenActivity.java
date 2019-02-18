package com.inspect.vehicle.base;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.inspect.vehicle.impl.IContactsImpl;
import com.loyal.base.ui.activity.ABasicFullScreenActivity;

import butterknife.ButterKnife;

public abstract class BaseFullScreenActivity extends ABasicFullScreenActivity implements IContactsImpl {
    private ActionBar actionBar;

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
