package com.inspect.vehicle.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract @LayoutRes
    int actLayout();

    public abstract void
    afterOnCreate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(actLayout());
        ButterKnife.bind(this);
        afterOnCreate();
    }
}
