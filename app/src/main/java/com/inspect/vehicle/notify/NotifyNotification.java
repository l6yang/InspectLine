package com.inspect.vehicle.notify;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.inspect.vehicle.R;

public class NotifyNotification {
    private static final String NOTIFICATION_TAG = "notify";

    public static void notify(final Context context, String message) {
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        final String title = res.getString(R.string.app_name);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_TAG)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker(res.getString(R.string.app_name))//弹出Notify的提示语
                .setAutoCancel(true);
        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != nm)
            nm.notify(NOTIFICATION_TAG, 0, notification);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != nm)
            nm.cancel(NOTIFICATION_TAG, 0);
    }
}
