package com.example.chapter13;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.util.PermissionUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_photo_take).setOnClickListener(this);
        findViewById(R.id.btn_photo_choose).setOnClickListener(this);
        findViewById(R.id.btn_image_change).setOnClickListener(this);
        findViewById(R.id.btn_image_decoder).setOnClickListener(this);
        findViewById(R.id.btn_image_special).setOnClickListener(this);
        findViewById(R.id.btn_audio_record).setOnClickListener(this);
        findViewById(R.id.btn_audio_play).setOnClickListener(this);
        findViewById(R.id.btn_media_recorder).setOnClickListener(this);
        findViewById(R.id.btn_video_record).setOnClickListener(this);
        findViewById(R.id.btn_video_choose).setOnClickListener(this);
        findViewById(R.id.btn_video_play).setOnClickListener(this);
        findViewById(R.id.btn_evaluate).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_photo_take) {
            if (PermissionUtil.checkPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, R.id.btn_photo_take % 65536)) {
                startActivity(new Intent(this, PhotoTakeActivity.class));
            }
        } else if (v.getId() == R.id.btn_photo_choose) {
            if (PermissionUtil.checkPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, R.id.btn_photo_choose % 65536)) {
                startActivity(new Intent(this, PhotoChooseActivity.class));
            }
        } else if (v.getId() == R.id.btn_image_change) {
            Intent intent = new Intent(this, ImageChangeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_image_decoder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Intent intent = new Intent(this, ImageDecoderActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "图像解码器需要Android 9.0或以上版本", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_image_special) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Intent intent = new Intent(this, ImageSpecialActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "图像解码器需要Android 9.0或以上版本", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_audio_record) {
            Intent intent = new Intent(this, AudioRecordActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_audio_play) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, R.id.btn_audio_play % 65536)) {
                startActivity(new Intent(this, AudioPlayActivity.class));
            }
        } else if (v.getId() == R.id.btn_media_recorder) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.RECORD_AUDIO, R.id.btn_media_recorder % 65536)) {
                startActivity(new Intent(this, MediaRecorderActivity.class));
            }
        } else if (v.getId() == R.id.btn_video_record) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.CAMERA, R.id.btn_video_record % 65536)) {
                startActivity(new Intent(this, VideoRecordActivity.class));
            }
        } else if (v.getId() == R.id.btn_video_choose) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.CAMERA, R.id.btn_video_choose % 65536)) {
                startActivity(new Intent(this, VideoChooseActivity.class));
            }
        } else if (v.getId() == R.id.btn_video_play) {
            Intent intent = new Intent(this, VideoPlayActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_evaluate) {
            if (PermissionUtil.checkPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, R.id.btn_evaluate % 65536)) {
                startActivity(new Intent(this, GoodsOrderActivity.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode不能为负数，也不能大于2的16次方即65536
        if (requestCode == R.id.btn_photo_take % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, PhotoTakeActivity.class));
            } else {
                Toast.makeText(this, "需要允许摄像头和存储卡权限才能拍照噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_photo_choose % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, PhotoChooseActivity.class));
            } else {
                Toast.makeText(this, "需要允许摄像头和存储卡权限才能选取照片噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_audio_play % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, AudioPlayActivity.class));
            } else {
                Toast.makeText(this, "需要允许存储卡权限才能播音噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_media_recorder % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, MediaRecorderActivity.class));
            } else {
                Toast.makeText(this, "需要允许录音权限才能录音噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_video_record % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, VideoRecordActivity.class));
            } else {
                Toast.makeText(this, "需要允许摄像头权限才能录像噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_video_choose % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, VideoChooseActivity.class));
            } else {
                Toast.makeText(this, "需要允许摄像头权限才能选取视频噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_evaluate % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, GoodsOrderActivity.class));
            } else {
                Toast.makeText(this, "需要允许摄像头和存储卡权限才能评价晒单噢", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
