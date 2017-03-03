package com.bluemobi.wanmen.activity;

import android.app.Application;
import android.util.Log;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.mistatistic.sdk.URLStatsRecorder;
import com.xiaomi.mistatistic.sdk.controller.HttpEventFilter;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;

/**
 * 作者： sunll 日期： 2015/9/24 10:00
 */
public class MyApplication extends Application {
    private String AppID = "2882303761517393091";
    private String AppKey = "5911739331091";
    private String AppSecret = "9l74AQQUipZz81evKqNYcQ==";

    @Override
    public void onCreate() {
        super.onCreate();
        MiStatInterface.initialize(this.getApplicationContext(), AppID, AppKey,
                "default channel");
        MiStatInterface.setUploadPolicy(
                MiStatInterface.UPLOAD_POLICY_REALTIME, 0);
        MiStatInterface.enableLog();

        // enable exception catcher.
        MiStatInterface.enableExceptionCatcher(true);

        // enable network monitor
        URLStatsRecorder.enableAutoRecord();
        URLStatsRecorder.setEventFilter(new HttpEventFilter() {

            @Override
            public HttpEvent onEvent(HttpEvent event) {
                Log.d("MI_STAT", event.getUrl() + " result =" + event.toJSON());
                // returns null if you want to drop this event.
                // you can modify it here too.
                return event;
            }
        });

        Log.d("MI_STAT", MiStatInterface.getDeviceID(this) + " is the device.");
    }

}
