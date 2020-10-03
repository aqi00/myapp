package com.example.chapter14;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class PhotoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "PhotoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        String title = getIntent().getStringExtra("title");
        String image_url = getIntent().getStringExtra("image_url");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ImageView iv_photo = findViewById(R.id.iv_photo);
        // 构建一个加载网络图片的建造器
        RequestBuilder<Drawable> builder = Glide.with(this).load(image_url)
                .transition(DrawableTransitionOptions.withCrossFade(1000)); // 设置时长1秒的渐变动画
        RequestOptions options = new RequestOptions(); // 创建Glide的请求选项
        options.override(Target.SIZE_ORIGINAL); // 展示原始图片
        options.disallowHardwareConfig(); // 关闭硬件加速，防止过大尺寸的图片加载报错
        // 在图像视图上展示网络图片。apply方法表示启用指定的请求选项
        builder.apply(options).into(iv_photo);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        }
    }
}
