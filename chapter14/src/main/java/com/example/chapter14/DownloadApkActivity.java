package com.example.chapter14;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter14.constant.ApkConstant;
import com.example.chapter14.util.DateUtil;

@SuppressLint({"SetTextI18n","DefaultLocale"})
public class DownloadApkActivity extends AppCompatActivity {
    private static final String TAG = "DownloadApkActivity";
    private Spinner sp_apk_url; // 安装包链接的下拉框
    private TextView tv_apk_result;
    private boolean isFirstSelect = true; // 是否首次选择
    private DownloadManager mDownloadManager; // 声明一个下载管理器对象
    private long mDownloadId = 0; // 下载编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_apk);
        tv_apk_result = findViewById(R.id.tv_apk_result);
        // 从系统服务中获取下载管理器
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        initApkSpinner(); // 初始化安装包链接的下拉框
    }

    // 初始化安装包链接的下拉框
    private void initApkSpinner() {
        ArrayAdapter<String> apkUrlAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, ApkConstant.NAME_ARRAY);
        sp_apk_url = findViewById(R.id.sp_apk_url);
        sp_apk_url.setPrompt("请选择要下载的安装包");
        sp_apk_url.setAdapter(apkUrlAdapter);
        sp_apk_url.setOnItemSelectedListener(new ApkUrlSelectedListener());
        sp_apk_url.setSelection(0);
    }

    class ApkUrlSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isFirstSelect) { // 刚打开页面时不需要执行下载动作
                isFirstSelect = false;
                return;
            }
            startDownload(arg2); // 开始下载指定序号的apk文件
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 开始下载指定序号的apk文件
    private void startDownload(int pos) {
        tv_apk_result.setText("正在下载" + ApkConstant.NAME_ARRAY[pos] +
                "的安装包，请到通知栏查看下载进度");
        Uri uri = Uri.parse(ApkConstant.URL_ARRAY[pos]); // 根据下载地址构建一个Uri对象
        Request down = new Request(uri); // 创建一个下载请求对象，指定从哪里下载文件
        down.setTitle(ApkConstant.NAME_ARRAY[pos] + "下载信息"); // 设置任务标题
        down.setDescription(ApkConstant.NAME_ARRAY[pos] + "安装包正在下载"); // 设置任务描述
        // 设置允许下载的网络类型
        down.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
        // 设置通知栏在下载进行时与完成后都可见
        down.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置下载文件在私有目录的保存路径。从Android10开始，只有保存到公共目录的才会在系统下载页面显示，保存到私有目录的不在系统下载页面显示
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, pos + ".apk");
        // 设置下载文件在公共目录的保存路径。保存到公共目录需要申请存储卡的读写权限
        //down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pos + ".apk");
        mDownloadId = mDownloadManager.enqueue(down); // 把下载请求对象加入到下载队列
    }

    // 定义一个下载完成的广播接收器。用于接收下载完成事件
    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) { // 下载完毕
                // 从意图中解包获得下载编号
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d(TAG, "下载完成 id : " + downId + ", mDownloadId=" + mDownloadId);
                tv_apk_result.setVisibility(View.VISIBLE);
                String desc = String.format("%s 编号%d的下载任务已完成", DateUtil.getNowTime(), downId);
                tv_apk_result.setText(desc); // 显示下载任务的完成描述
            }
        }
    }

    // 定义一个通知栏点击的广播接收器。用于接收下载通知栏的点击事件，在下载过程中有效，下载完成后失效
    private class NotificationClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) { // 点击了通知栏
                // 从意图中解包获得被点击通知的下载编号
                long[] downIds = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                for (long downId : downIds) {
                    Log.d(TAG, "点击通知 id : " + downId + ", mDownloadId=" + mDownloadId);
                    if (downId == mDownloadId) { // 找到当前的下载任务
                        String desc = String.format("%s 点击了编号%d的下载通知", DateUtil.getNowTime(), downId);
                        tv_apk_result.setText(desc); // 显示下载任务的点击描述
                    }
                }
            }
        }
    }

    private DownloadCompleteReceiver completeReceiver; // 声明一个下载完成的广播接收器
    private NotificationClickReceiver clickReceiver; // 声明一个通知栏点击的广播接收器

    @Override
    public void onStart() {
        super.onStart();
        completeReceiver = new DownloadCompleteReceiver(); // 创建一个下载完成的广播接收器
        // 注册接收器，注册之后才能正常接收广播
        registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        clickReceiver = new NotificationClickReceiver(); // 创建一个通知栏点击的广播接收器
        // 注册接收器，注册之后才能正常接收广播
        registerReceiver(clickReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(completeReceiver); // 注销下载完成的广播接收器
        unregisterReceiver(clickReceiver); // 注销通知栏点击的广播接收器
    }

}
