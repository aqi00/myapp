package com.example.chapter13;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@TargetApi(Build.VERSION_CODES.P)
public class ImageSpecialActivity extends AppCompatActivity {
    private TextView tv_info; // 声明一个文本视图对象
    private ImageView iv_pic; // 声明一个图像视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_special);
        tv_info = findViewById(R.id.tv_info);
        iv_pic = findViewById(R.id.iv_pic);
        initTypeSpinner(); // 初始化图像类型下拉框
    }

    // 初始化图像类型下拉框
    private void initTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, typeArray);
        Spinner sp_type = findViewById(R.id.sp_type);
        sp_type.setPrompt("请选择图像类型");
        sp_type.setAdapter(typeAdapter);
        sp_type.setOnItemSelectedListener(new ImageSpecialActivity.ImageTypeListener());
        sp_type.setSelection(0);
    }

    private String[] typeArray = {"直接显示GIF", "直接显示WebP", "显示GIF动图", "显示WebP动图", "显示HEIF图片"};
    class ImageTypeListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (arg2 == 0) {
                tv_info.setText("");
                iv_pic.setImageResource(R.drawable.happy);
            } else if (arg2 == 1) {
                tv_info.setText("");
                iv_pic.setImageResource(R.drawable.world_cup_2014);
            } else if (arg2 == 2) {
                showImage(R.drawable.happy); // 显示gif和webp图片
            } else if (arg2 == 3) {
                showImage(R.drawable.world_cup_2014); // 显示gif和webp图片
            } else if (arg2 == 4) {
                showHeic(R.raw.lotus); // 显示Heif图片（扩展名为heif或者heic）
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 显示Heif图片（扩展名为heif或者heic）
    private void showHeic(int imageId) {
        try (InputStream is = getResources().openRawResource(imageId)) { // 从资源文件中获取输入流对象
            byte[] bytes = new byte[is.available()]; // 创建临时存放的字节数组
            is.read(bytes); // 从输入流中读取字节数组
            // 利用Android 9.0新增的ImageDecoder读取图片
            ImageDecoder.Source source = ImageDecoder.createSource(ByteBuffer.wrap(bytes));
            showImageSource(source); // 显示指定来源的图像
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示gif和webp图片
    private void showImage(int imageId) {
        try {
            // 利用Android 9.0新增的ImageDecoder读取图片
            ImageDecoder.Source source = ImageDecoder.createSource(getResources(), imageId);
            showImageSource(source); // 显示指定来源的图像
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示指定来源的图像
    private void showImageSource(ImageDecoder.Source source) throws IOException {
        // 从数据源解码得到图形信息
        Drawable drawable = ImageDecoder.decodeDrawable(source, new OnHeaderDecodedListener() {
            @Override
            public void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
                // 获取图像信息的媒体类型与是否动图
                String desc = String.format("该图片类型为%s，它%s动图",
                        info.getMimeType(), info.isAnimated()?"是":"不是");
                tv_info.setText(desc);
            }
        });
        iv_pic.setImageDrawable(drawable); // 设置图像视图的图形对象
        if (drawable instanceof Animatable) { // 如果是动画图形，则开始播放动画
            ((Animatable) iv_pic.getDrawable()).start();
        }
    }

}