package com.example.chapter09;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter09.util.DateUtil;
import com.example.chapter09.util.NetworkUtil;

public class SystemNetworkActivity extends AppCompatActivity {
    private TextView tv_network; // 声明一个文本视图对象
    private String desc = "开始侦听网络变更广播，请开关WLAN或者数据连接，再观察广播结果";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_network);
        tv_network = findViewById(R.id.tv_network);
        tv_network.setText(desc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkReceiver = new NetworkReceiver(); // 创建一个网络变更的广播接收器
        // 创建一个意图过滤器，只处理网络状态变化的广播
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, filter); // 注册接收器，注册之后才能正常接收广播
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver); // 注销接收器，注销之后就不再接收广播
    }

    private NetworkReceiver networkReceiver; // 声明一个网络变更的广播接收器实例
    // 定义一个网络变更的广播接收器
    private class NetworkReceiver extends BroadcastReceiver {
        // 一旦接收到网络变更的广播，马上触发接收器的onReceive方法
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                NetworkInfo networkInfo = intent.getParcelableExtra("networkInfo");
                String networkClass = NetworkUtil.getNetworkClass(networkInfo.getSubtype());
                desc = String.format("%s\n%s 收到一个网络变更广播，网络大类为%s，" +
                                "网络小类为%s，网络制式为%s，网络状态为%s",
                        desc, DateUtil.getNowTime(), networkInfo.getTypeName(),
                        networkInfo.getSubtypeName(), networkClass,
                        networkInfo.getState().toString());
                tv_network.setText(desc);
            }
        }
    }

}
