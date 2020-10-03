package com.example.chapter14;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter14.task.UploadTask;
import com.example.chapter14.task.UploadTask.OnUploadListener;
import com.example.chapter14.constant.UrlConstant;
import com.example.chapter14.util.DateUtil;
import com.example.chapter14.util.FileUtil;

@SuppressLint("SetTextI18n")
public class HttpUploadActivity extends AppCompatActivity implements View.OnClickListener, OnUploadListener {
    private final static String TAG = "HttpUploadActivity";
    private int CHOOSE_CODE = 3; // 只在相册挑选图片的请求码
    private TextView tv_file_path;
    private String mFilePath; // 图片文件的路径

    @Override
    protected void onCreate(Bundle selectdInstanceState) {
        super.onCreate(selectdInstanceState);
        setContentView(R.layout.activity_http_upload);
        tv_file_path = findViewById(R.id.tv_file_path);
        findViewById(R.id.btn_file_select).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_file_select) {
            // 创建一个内容获取动作的意图（准备跳到系统相册）
            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            albumIntent.setType("image/*"); // 类型为图像
            startActivityForResult(albumIntent, CHOOSE_CODE); // 打开系统相册
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) { // 从相册回来
            if (intent.getData() != null) { // 从相册选择一张照片
                Uri uri = intent.getData(); // 获得已选择照片的路径对象
                // 获得图片的临时保存路径
                mFilePath = String.format("%s/%s.jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo_"+ DateUtil.getNowDateTime());
                FileUtil.saveFileFromUri(this, uri, mFilePath); // 保存为临时文件
                tv_file_path.setText("上传文件的路径为：" + mFilePath);
                UploadTask task = new UploadTask(); // 创建文件上传线程
                task.setOnUploadListener(this); // 设置文件上传监听器
                task.execute(mFilePath); // 把文件上传线程加入到处理队列
            }
        }
    }

    // 在文件上传结束后触发
    public void finishUpload(String result) {
        // 以下拼接文件上传的结果描述
        String desc = String.format("上传文件的路径：%s\n上传结果：%s\n预计下载地址：%s%s",
                mFilePath, (TextUtils.isEmpty(result))?"失败":result,
                UrlConstant.REQUEST_URL, mFilePath.substring(mFilePath.lastIndexOf("/")));
        tv_file_path.setText(desc);
        Log.d(TAG, desc);
    }

}
