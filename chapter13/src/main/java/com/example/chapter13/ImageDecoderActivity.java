package com.example.chapter13;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

@TargetApi(Build.VERSION_CODES.P)
public class ImageDecoderActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ImageDecoderActivity";
    private ImageView iv_photo; // 声明一个图像视图对象
    private int CHOOSE_CODE = 3; // 选择照片的请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_decoder);
        iv_photo = findViewById(R.id.iv_photo);
        findViewById(R.id.btn_choose).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_choose) {
            // 创建一个内容获取动作的意图（准备跳到系统相册）
            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            albumIntent.setType("image/*"); // 设置内容类型为图像
            startActivityForResult(albumIntent, CHOOSE_CODE); // 打开系统相册
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) {
            if (intent.getData() != null) { // 从相册选择一张照片
                Uri imageUri = intent.getData();
                showDecodedImage(imageUri); // 显示解码后的图像
            }
        }
    }

    // 显示解码后的图像
    private void showDecodedImage(Uri imageUri) {
        try {
            // 利用Android 9.0新增的ImageDecoder读取图片
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
            // 从数据源解码得到图形信息
            Drawable drawable = ImageDecoder.decodeDrawable(source);
            iv_photo.setImageDrawable(drawable); // 设置图像视图的图形对象
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}