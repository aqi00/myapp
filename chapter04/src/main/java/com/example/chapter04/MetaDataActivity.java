package com.example.chapter04;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MetaDataActivity extends AppCompatActivity {
    private TextView tv_meta; // 声明一个文本视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_data);
        // 从布局文件中获取名叫tv_meta的文本视图
        tv_meta = findViewById(R.id.tv_meta);
        showMetaData(); // 显示配置的元数据
    }

    // 显示配置的元数据
    private void showMetaData() {
        try {
            PackageManager pm = getPackageManager(); // 获取应用包管理器
            // 从应用包管理器中获取当前的活动信息
            ActivityInfo act = pm.getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Bundle bundle = act.metaData; // 获取活动附加的元数据信息
            String value = bundle.getString("weather"); // 从包裹中取出名叫weather的字符串
            tv_meta.setText("来自元数据信息：今天的天气是"+value); // 在文本视图上显示文字
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
