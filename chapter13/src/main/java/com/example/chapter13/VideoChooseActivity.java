package com.example.chapter13;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.util.FileUtil;
import com.example.chapter13.util.MediaUtil;

public class VideoChooseActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "VideoChooseActivity";
    private int CHOOSE_CODE = 3; // 只在视频库挑选图片的请求码
    private int COMBINE_CODE = 4; // 既可录像获得现场视频、也可在视频库挑选已有视频的请求码
    private TextView tv_video;
    private RelativeLayout rl_video;
    private ImageView iv_video;
    private Uri mVideoUri; // 视频文件的路径对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_choose);
        tv_video = findViewById(R.id.tv_video);
        rl_video = findViewById(R.id.rl_video);
        iv_video = findViewById(R.id.iv_video);
        findViewById(R.id.btn_choose).setOnClickListener(this);
        findViewById(R.id.btn_combine).setOnClickListener(this);
        findViewById(R.id.rl_video).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_choose) {
            // 创建一个内容获取动作的意图（准备跳到系统视频库）
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
            startActivityForResult(intent, CHOOSE_CODE); // 打开系统视频库
        } else if (v.getId() == R.id.btn_combine) {
            openSelectDialog(); // 打开选择对话框（要录像还是去视频库）
        } else if (v.getId() == R.id.rl_video) {
            String realPath = FileUtil.getPathFromContentUri(this, mVideoUri);
            // 创建一个内容获取动作的意图（准备跳到系统播放器）
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(realPath), "video/*"); // 类型为视频
            startActivity(intent); // 打开系统的视频播放器
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode==RESULT_OK && requestCode==CHOOSE_CODE) { // 从视频库回来
            if (intent.getData() != null) { // 选择一个视频
                Uri uri = intent.getData(); // 获得已选择视频的路径对象
                showVideoFrame(uri); // 显示视频的某帧图片
            } else if (intent.getClipData() != null) { // 选择多个视频
                ClipData videos = intent.getClipData(); // 获取剪切板数据
                if (videos.getItemCount() > 0) { // 至少选择了一个文件
                    Uri uri = videos.getItemAt(0).getUri(); // 取第一个视频
                    showVideoFrame(uri); // 显示视频的某帧图片
                }
            }
        }
        if (resultCode==RESULT_OK && requestCode==COMBINE_CODE) { // 从混合选择对话框回来
            if (intent.getData() != null) { // 录像或者从视频库选择一个视频
                Uri uri = intent.getData(); // 获得已选择视频的路径对象
                showVideoFrame(uri); // 显示视频的某帧图片
            }
        }
    }

    // 打开选择对话框（要录像还是去视频库）
    private void openSelectDialog() {
        // 声明摄像机的录像行为
        Intent recordIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent[] intentArray = new Intent[] { recordIntent };
        // 声明视频库的打开行为
        Intent videoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        videoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
        // 弹出含摄像机和视频库在内的列表对话框
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "请录像或选择视频");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, videoIntent);
        // 在页面底部弹出多种选择方式的列表对话框
        startActivityForResult(Intent.createChooser(chooserIntent, "选择视频"), COMBINE_CODE);
    }

    // 显示视频的某帧图片
    private void showVideoFrame(Uri uri) {
        mVideoUri = uri;
        tv_video.setText("你选中的视频地址为："+uri.toString());
        rl_video.setVisibility(View.VISIBLE);
        // 获取视频文件的某帧图片
        Bitmap bitmap = MediaUtil.getOneFrame(this, uri);
        iv_video.setImageBitmap(bitmap); // 设置图像视图的位图对象
    }
    
}
