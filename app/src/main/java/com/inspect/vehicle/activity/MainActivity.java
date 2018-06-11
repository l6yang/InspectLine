package com.inspect.vehicle.activity;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void afterOnCreate() {
        setActionBack(false);
    }
}
