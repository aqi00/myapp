package com.example.chapter09;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.widget.TextView;

import com.example.chapter09.util.DateUtil;

public class ReturnDesktopActivity extends AppCompatActivity {
    private final static String TAG = "ReturnDesktopActivity";
    private TextView tv_monitor;
    private String mDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_desktop);
        tv_monitor = findViewById(R.id.tv_monitor);
        initDesktopRecevier(); // 初始化桌面广播
    }

    // 显示变更的状态
    private void showChangeStatus(String reason) {
        mDesc = String.format("%s%s 按下了%s键\n", mDesc, DateUtil.getNowTime(), reason);
        tv_monitor.setText(mDesc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && !isInPictureInPictureMode()) { // 当前未开启画中画，则开启画中画模式
            // 创建画中画模式的参数构建器
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            // 设置宽高比例值，第一个参数表示分子，第二个参数表示分母
            // 下面的10/5=2，表示画中画窗口的宽度是高度的两倍
            Rational aspectRatio = new Rational(10,5);
            builder.setAspectRatio(aspectRatio); // 设置画中画窗口的宽高比例
            // 进入画中画模式，注意enterPictureInPictureMode是Android8.0之后新增的方法
            enterPictureInPictureMode(builder.build());
        }
    }

    // 在进入画中画模式或退出画中画模式时触发
    @Override
    public void onPictureInPictureModeChanged(boolean isInPicInPicMode, Configuration newConfig) {
        Log.d(TAG, "onPictureInPictureModeChanged isInPicInPicMode="+isInPicInPicMode);
        super.onPictureInPictureModeChanged(isInPicInPicMode, newConfig);
        if (isInPicInPicMode) { // 进入画中画模式
        } else { // 退出画中画模式
        }
    }

    // 初始化桌面广播
    private void initDesktopRecevier() {
        desktopRecevier = new DesktopRecevier(); // 创建一个返回桌面的广播接收器
        // 创建一个意图过滤器，只接收关闭系统对话框（即返回桌面）的广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(desktopRecevier, intentFilter); // 注册接收器，注册之后才能正常接收广播
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(desktopRecevier); // 注销接收器，注销之后就不再接收广播
    }

    private DesktopRecevier desktopRecevier; // 声明一个返回桌面的广播接收器对象
    // 定义一个返回到桌面的广播接收器
    private class DesktopRecevier extends BroadcastReceiver {
        // 在收到返回桌面广播时触发
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason"); // 获取变更原因
                // 按下了主页键或者任务键
                if (!TextUtils.isEmpty(reason) && (reason.equals("homekey")
                        || reason.equals("recentapps"))) {
                    showChangeStatus(reason); // 显示变更的状态
                }
            }
        }
    }
}
