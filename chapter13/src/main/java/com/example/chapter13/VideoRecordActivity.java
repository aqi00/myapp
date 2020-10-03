package com.example.chapter13;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.util.DateUtil;
import com.example.chapter13.util.FileUtil;
import com.example.chapter13.util.MediaUtil;

public class VideoRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "VideoRecordActivity";
    private int RECORDER_CODE = 1; // 录制操作的请求码
    private TextView tv_video;
    private RelativeLayout rl_video;
    private ImageView iv_video;
    private Uri mVideoUri; // 视频文件的路径对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        tv_video = findViewById(R.id.tv_video);
        rl_video = findViewById(R.id.rl_video);
        iv_video = findViewById(R.id.iv_video);
        findViewById(R.id.btn_recorder).setOnClickListener(this);
        findViewById(R.id.rl_video).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_recorder) {
            // 下面准备跳到系统的摄像机，并获得录制完的视频文件
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // 视频质量。0 低质量；1 高质量
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760L); // 大小限制，单位字节
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10); // 时长限制，单位秒
            startActivityForResult(intent, RECORDER_CODE); // 打开系统摄像机
        } else if (v.getId() == R.id.rl_video) {
            // 创建一个内容获取动作的意图（准备跳到系统播放器）
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(mVideoUri, "video/*"); // 类型为视频
            startActivity(intent); // 打开系统的视频播放器
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode==RESULT_OK && requestCode==RECORDER_CODE){ // 从摄像机返回
            mVideoUri = intent.getData(); // 获得已录制视频的路径对象
//            // 生成临时视频的保存路径
//            String filePath = String.format("%s/%s.mp4",
//                    getExternalFilesDir(Environment.DIRECTORY_MOVIES), "video_"+ DateUtil.getNowDateTime());
//            // 把录制完的视频保存到临时路径
//            FileUtil.saveFileFromUri(this, mVideoUri, filePath);
            tv_video.setText("录制完成的视频地址为："+mVideoUri.toString());
            rl_video.setVisibility(View.VISIBLE);
            // 获取视频文件的某帧图片
            Bitmap bitmap = MediaUtil.getOneFrame(this, mVideoUri);
            iv_video.setImageBitmap(bitmap); // 设置图像视图的位图对象
        }
    }

}
