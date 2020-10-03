package com.example.chapter04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReadStringActivity extends AppCompatActivity {
    private TextView tv_resource; // 声明一个文本视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_string);
        // 从布局文件中获取名叫tv_resource的文本视图
        tv_resource = findViewById(R.id.tv_resource);
        showStringResource(); // 显示字符串资源
    }

    // 显示字符串资源
    private void showStringResource() {
        String value = getString(R.string.weather_str); // 从strings.xml获取名叫weather_str的字符串值
        tv_resource.setText("来自字符串资源：今天的天气是"+value); // 在文本视图上显示文字
    }
}
