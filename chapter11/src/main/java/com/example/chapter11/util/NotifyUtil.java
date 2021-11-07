package com.example.chapter11.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotifyUtil {
    private final static String TAG = "NotifyUtil";

    @TargetApi(Build.VERSION_CODES.O)
    // 创建通知渠道。Android 8.0开始必须给每个通知分配对应的渠道
    public static void createNotifyChannel(Context ctx, String channelId, String channelName, int importance) {
        // 从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyMgr.getNotificationChannel(channelId) == null) { // 已经存在指定编号的通知渠道
            // 创建指定编号、指定名称、指定级别的通知渠道
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setSound(null, null); // 设置推送通知之时的铃声。null表示静音推送
            channel.enableLights(true); // 通知渠道是否让呼吸灯闪烁
            channel.enableVibration(true); // 通知渠道是否让手机震动
            channel.setShowBadge(true); // 通知渠道是否在应用图标的右上角展示小红点
            // VISIBILITY_PUBLIC显示所有通知信息，VISIBILITY_PRIVATE只显示通知标题不显示通知内容，VISIBILITY_SECRET不显示任何通知信息
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); // 设置锁屏时候的可见性
            channel.setImportance(importance); // 设置通知渠道的重要性级别
            notifyMgr.createNotificationChannel(channel); // 创建指定的通知渠道
        }
    }

    // 在桌面上的应用图标右上角显示数字角标
    public static void showMarkerCount(Context ctx, int count, Notification notify) {
        showBadgeOfEMUI(ctx, count); // 华为手机EMUI系统的消息角标
        // 小米手机还要进入设置里面的应用管理，开启当前App的“显示桌面图标角标”
        showBadgeOfMIUI(count, notify); // 小米手机MIUI系统的消息角标
    }

    // 华为的消息角标需要事先声明两个权限：android.permission.INTERNET、com.huawei.android.launcher.permission.CHANGE_BADGE
    private static void showBadgeOfEMUI(Context ctx, int count) {
        try {
            Bundle extra = new Bundle(); // 创建一个包裹对象
            extra.putString("package", ctx.getPackageName()); // 应用的包名
            // 应用的首屏页面路径
            extra.putString("class", ctx.getPackageName()+".MainActivity");
            extra.putInt("badgenumber", count); // 应用的消息数量
            Uri uri = Uri.parse("content://com.huawei.android.launcher.settings/badge/");
            // 通过内容解析器调用华为内核的消息角标服务
            ctx.getContentResolver().call(uri, "change_badge", null, extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 小米的消息角标需要在发送通知的时候一块调用
    private static void showBadgeOfMIUI(int count, Notification notify) {
        try {
            // 利用反射技术获得额外的新增字段extraNotification
            Field field = notify.getClass().getDeclaredField("extraNotification");
            Log.d(TAG, "field.getName="+field.getName());
            // 该字段为Notification类型，下面获取它的实例对象
            Object extra = field.get(notify);
            Log.d(TAG, "extraNotification.toString="+extra.toString());
            // 利用反射技术获得额外的新增方法setMessageCount
            Method method = extra.getClass().getDeclaredMethod("setMessageCount", int.class);
            Log.d(TAG, "method.getName="+method.getName());
            // 利用反射技术调用实例对象的setMessageCount方法，设置消息角标的数量
            method.invoke(extra, count);
            Log.d(TAG, "invoke count="+count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
