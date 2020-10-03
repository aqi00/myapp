package com.example.chapter09;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter09.util.DateUtil;

public class SystemMinuteActivity extends AppCompatActivity {
    private TextView tv_minute; // 声明一个文本视图对象
    private String desc = "开始侦听分钟广播，请稍等。注意要保持屏幕亮着，才能正常收到广播";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_minute);
        tv_minute = findViewById(R.id.tv_minute);
        tv_minute.setText(desc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeReceiver = new TimeReceiver(); // 创建一个分钟变更的广播接收器
        // 创建一个意图过滤器，只处理系统分钟变化的广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filter); // 注册接收器，注册之后才能正常接收广播
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(timeReceiver); // 注销接收器，注销之后就不再接收广播
    }

    private TimeReceiver timeReceiver; // 声明一个分钟广播的接收器实例
    // 定义一个分钟广播的接收器
    private class TimeReceiver extends BroadcastReceiver {
        // 一旦接收到分钟变更的广播，马上触发接收器的onReceive方法
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                desc = String.format("%s\n%s 收到一个分钟到达广播%s", desc,
                        DateUtil.getNowTime(), intent.getAction());
                tv_minute.setText(desc);
            }
        }
    }

}
