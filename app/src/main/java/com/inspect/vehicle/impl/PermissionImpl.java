package com.inspect.vehicle.impl;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface PermissionImpl {
    PermissionImpl init(Context context);

    PermissionImpl init(Fragment fragment);

    PermissionImpl init(android.app.Fragment fragment);

    PermissionImpl requestPermissions(@IntRange(from = 0) int requestCode, @NonNull String[] permissions);

    void request();
}
