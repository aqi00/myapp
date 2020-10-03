package com.example.chapter13;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EvaluatePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "EvaluatePhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_photo);
        String image_path = getIntent().getStringExtra("image_path"); // 获取意图中的图片路径
        ImageView iv_photo = findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);
        iv_photo.setImageURI(Uri.parse(image_path)); // 设置图像视图的图片路径
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_photo) {
            finish(); // 关闭当前页面
        }
    }
}
