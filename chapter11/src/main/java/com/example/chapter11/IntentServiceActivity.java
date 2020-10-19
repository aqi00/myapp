package com.example.chapter11;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter11.service.AsyncService;
import com.example.chapter11.util.DateUtil;

@SuppressLint("SetTextI18n")
public class IntentServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_intent;
    private Handler mHandler = new Handler(Looper.myLooper()); // 声明一个处理器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
        tv_intent = findViewById(R.id.tv_intent);
        findViewById(R.id.btn_intent).setOnClickListener(this);
        mHandler.postDelayed(mService, 100); // 延迟100毫秒后启动异步任务
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_intent) {
            tv_intent.setText(DateUtil.getNowTime() + " 您轻轻点了一下下(异步服务正在运行，不影响您在界面操作)");
        }
    }

    private Runnable mService = new Runnable() {
        @Override
        public void run() {
            // 构建通往异步服务的意图
            Intent intent = new Intent(IntentServiceActivity.this, AsyncService.class);
            startService(intent); // 启动意图设定的异步服务
        }
    };

}
