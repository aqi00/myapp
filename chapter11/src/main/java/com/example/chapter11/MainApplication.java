package com.example.chapter11;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.chapter11.util.NotifyUtil;

public class MainApplication extends Application {
    private final static String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里不能屏蔽通知渠道代码，因为后面活动会给该渠道发送通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            NotifyUtil.createNotifyChannel(this, getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
        }
        Log.d(TAG, "onCreate");
    }

}
