package com.inspect.vehicle.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.inspect.vehicle.impl.IContactsImpl;
import com.inspect.vehicle.notify.DownNotification;
import com.inspect.vehicle.notify.NotifyNotification;
import com.inspect.vehicle.util.FileUtil;
import com.loyal.base.download.DownLoadAPI;
import com.loyal.base.download.DownLoadBean;
import com.loyal.base.download.DownLoadListener;
import com.loyal.base.impl.IBaseContacts;
import com.loyal.kit.DeviceUtil;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class DownloadService extends IntentService implements IContactsImpl, DownLoadListener, Observer<InputStream> {

    private static final String ACTION = "service.action.DownLoad";
    private Disposable disposable;
    private final File file = new File(Path.APK, FileUtil.apkName);


    public DownloadService() {
        super("DownloadService");
    }

    /**
     * @param statusBar 是否通知栏点击更新
     */
    public static void startAction(Context context, Intent intent, boolean statusBar) {
        intent.setClass(context, DownloadService.class);
        intent.setAction(ACTION);
        if (statusBar)
            context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (TextUtils.equals(ACTION, action)) {
                final String apkUrl = intent.getStringExtra("apkUrl");
                handleAction(apkUrl);
            }
        }
    }

    private void handleAction(String apkUrl) {
        Observable<ResponseBody> observable = DownLoadAPI.getInstance(this).getDownService().downLoad(apkUrl);
        DownLoadAPI.saveFile(observable, file, this);
    }

    private void dispose() {
        if (null != disposable && !disposable.isDisposed())
            disposable.dispose();
    }

    private void downloadCompleted(boolean completed, String state) {
        DownLoadBean download = new DownLoadBean();
        download.setState(completed ? "complete" : state);
        sendIntent(download);
    }

    /**
     * 界面和通知栏进度同步
     */
    private void sendIntent(DownLoadBean download) {
        String state = IBaseContacts.BaseStr.replaceNull(download.getState());
        int progress = download.getProgress();
        if (TextUtils.equals("loading", state)) {
            String size = DeviceUtil.getDataSize(download.getCurrentFileSize());
            String total = DeviceUtil.getDataSize(download.getTotalFileSize());
            String current = String.format("%s / %s", size, total);
            State.UPDATE = State.UPDATE_ING;
            DownNotification.notify(this, progress, current);
        } else if (TextUtils.equals("complete", state)) {
            DownNotification.cancel(this);
            File file = new File(Path.APK, FileUtil.apkName);
            if (!file.exists()) {
                State.UPDATE = State.UPDATE_FAIL;
                NotifyNotification.notify(this, "安装失败，文件不存在");
            } else {
                State.UPDATE = State.UPDATE_SUCCESS;
                com.inspect.vehicle.util.DeviceUtil.install(this, file);
            }
        } else {
            State.UPDATE = State.UPDATE_FAIL;
            DownNotification.cancel(this);
            NotifyNotification.notify(this, "下载失败");
        }
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        int progress = (int) (bytesRead * 100f / contentLength);
        if (progress > 0) {
            DownLoadBean download = new DownLoadBean();
            download.setTotalFileSize(contentLength);
            download.setCurrentFileSize(bytesRead);
            download.setProgress(progress);
            download.setState("loading");
            sendIntent(download);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(InputStream inputStream) {
    }

    @Override
    public void onError(Throwable e) {
        dispose();
        downloadCompleted(false, e.getMessage());
    }

    @Override
    public void onComplete() {
        dispose();
        downloadCompleted(true, file.getPath());
    }
}
