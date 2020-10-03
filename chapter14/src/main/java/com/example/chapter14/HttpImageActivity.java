package com.example.chapter14;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter14.task.GetImageCodeTask;
import com.example.chapter14.task.GetImageCodeTask.OnImageCodeListener;

public class HttpImageActivity extends AppCompatActivity implements View.OnClickListener, OnImageCodeListener {
    private ImageView iv_image_code;
    private boolean isRunning = false; // 是否正在运行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_image);
        iv_image_code = findViewById(R.id.iv_image_code);
        iv_image_code.setOnClickListener(this);
        getImageCode(); // 获取图片验证码
    }

    // 获取图片验证码
    private void getImageCode() {
        if (!isRunning) {
            isRunning = true;
            GetImageCodeTask task = new GetImageCodeTask(this); // 创建验证码获取的异步任务
            task.setOnImageCodeListener(this); // 设置验证码获取的监听器
            task.execute(); // 把验证码获取任务加入到处理队列
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_image_code) {
            getImageCode(); // 获取图片验证码
        }
    }

    // 在得到验证码后触发
    @Override
    public void onGetCode(String path) {
        iv_image_code.setImageURI(Uri.parse(path)); // 设置图像视图的图片路径
        isRunning = false;
    }

}
