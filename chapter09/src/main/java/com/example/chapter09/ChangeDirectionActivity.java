package com.example.chapter09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chapter09.util.DateUtil;

public class ChangeDirectionActivity extends AppCompatActivity {
    private TextView tv_monitor; // 声明一个文本视图对象
    private String mDesc = ""; // 屏幕变更的描述说明

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_direction);
        tv_monitor = findViewById(R.id.tv_monitor);
    }

    // 在配置项变更时触发。比如屏幕方向发生变更等等
    // 有的手机需要在系统的“设置→显示”菜单开启“自动旋转屏幕”，或者从顶部下拉，找到“自动旋转”图标并开启
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) { // 判断当前的屏幕方向
            case Configuration.ORIENTATION_PORTRAIT: // 切换到竖屏
                mDesc = String.format("%s%s %s\n", mDesc,
                        DateUtil.getNowTime(), "当前屏幕为竖屏方向");
                tv_monitor.setText(mDesc);
                break;
            case Configuration.ORIENTATION_LANDSCAPE: // 切换到横屏
                mDesc = String.format("%s%s %s\n", mDesc,
                        DateUtil.getNowTime(), "当前屏幕为横屏方向");
                tv_monitor.setText(mDesc);
                break;
            default:
                break;
        }
    }
}
