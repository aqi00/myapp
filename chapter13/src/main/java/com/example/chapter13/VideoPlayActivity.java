package com.example.chapter13;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "VideoPlayActivity";
    private VideoView vv_content; // 声明一个视频视图对象
    private int CHOOSE_CODE = 3; // 只在视频库挑选图片的请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        // 从布局文件中获取名叫vv_content的视频视图
        vv_content = findViewById(R.id.vv_content);
        findViewById(R.id.btn_choose).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_choose) {
            // 创建一个内容获取动作的意图（准备跳到系统视频库）
            // ACTION_GET_CONTENT只可选择近期的视频
            //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // ACTION_PICK可选择所有视频
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*"); // 类型为视频
            startActivityForResult(intent, CHOOSE_CODE); // 打开系统视频库
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) {
            if (intent.getData() != null) { // 从视频库回来
                vv_content.setVideoURI(intent.getData()); // 设置视频视图的视频路径
                MediaController mc = new MediaController(this); // 创建一个媒体控制条
                vv_content.setMediaController(mc); // 给视频视图设置相关联的媒体控制条
                mc.setMediaPlayer(vv_content); // 给媒体控制条设置相关联的视频视图
                vv_content.start(); // 视频视图开始播放
            }
        }
    }

}
