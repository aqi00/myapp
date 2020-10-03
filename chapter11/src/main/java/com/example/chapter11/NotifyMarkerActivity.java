package com.example.chapter11;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter11.util.NotifyUtil;
import com.example.chapter11.util.ViewUtil;

public class NotifyMarkerActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_title;
    private EditText et_message;
    private EditText et_count;
    private static String mChannelId = "3"; // 通知渠道的编号
    private static String mChannelName = "一般重要"; // 通知渠道的名称
    private static int mImportance = NotificationManager.IMPORTANCE_DEFAULT; // 通知渠道的级别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_marker);
        et_title = findViewById(R.id.et_title);
        et_message = findViewById(R.id.et_message);
        et_count = findViewById(R.id.et_count);
        findViewById(R.id.btn_show_marker).setOnClickListener(this);
        findViewById(R.id.btn_clear_marker).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ViewUtil.hideOneInputMethod(this, et_message); // 隐藏输入法软键盘
        if (TextUtils.isEmpty(et_title.getText())) {
            Toast.makeText(this, "请填写消息标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_message.getText())) {
            Toast.makeText(this, "请填写消息内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_count.getText())) {
            Toast.makeText(this, "请填写消息数量", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = et_title.getText().toString();
        String message = et_message.getText().toString();
        int count = Integer.parseInt(et_count.getText().toString());
        if (v.getId() == R.id.btn_show_marker) {
            sendChannelNotify(title, message, count); // 发送指定渠道的通知消息
            Toast.makeText(this, "已显示消息角标，请回到桌面查看", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_clear_marker) {
            sendChannelNotify(title, message, 0); // 发送指定渠道的通知消息
            Toast.makeText(this, "已清除消息角标，请回到桌面查看", Toast.LENGTH_SHORT).show();
        }
    }

    // 发送指定渠道的通知消息（包括消息标题和消息内容）
    private void sendChannelNotify(String title, String message, int count) {
        // 创建一个跳转到活动页面的意图
        Intent clickIntent = new Intent(this, MainActivity.class);
        // 创建一个用于页面跳转的延迟意图
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.string.app_name, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 创建一个通知消息的建造器
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            builder = new Notification.Builder(this, mChannelId);
        }
        builder.setContentIntent(contentIntent) // 设置内容的点击意图
                .setAutoCancel(true) // 点击通知栏后是否自动清除该通知
                .setSmallIcon(R.mipmap.ic_launcher) // 设置应用名称左边的小图标
                .setContentTitle(title) // 设置通知栏里面的标题文本
                .setContentText(message); // 设置通知栏里面的内容文本
        Notification notify = builder.build(); // 根据通知建造器构建一个通知对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotifyUtil.createNotifyChannel(this, mChannelId, mChannelName, mImportance);
        }
        NotifyUtil.showMarkerCount(this, count, notify); // 在桌面的应用图标右上方显示指定数字的消息角标
        // 从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 使用通知管理器推送通知，然后在手机的通知栏就会看到该消息，多条通知需要指定不同的通知编号
        notifyMgr.notify(Integer.parseInt(mChannelId), notify);
    }

}
