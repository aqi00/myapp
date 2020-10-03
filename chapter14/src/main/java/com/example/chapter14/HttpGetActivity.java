package com.example.chapter14;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.chapter14.task.GetAddressTask;
import com.example.chapter14.task.GetAddressTask.OnAddressListener;
import com.example.chapter14.util.DateUtil;
import com.example.chapter14.util.SwitchUtil;

@SuppressLint("DefaultLocale")
public class HttpGetActivity extends AppCompatActivity implements OnAddressListener {
    private final static String TAG = "HttpGetActivity";
    private TextView tv_location;
    private Location mLocation; // 定位信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request);
        tv_location = findViewById(R.id.tv_location);
        // 检查定位功能是否打开，若未打开则跳到系统的定位功能设置页面
        SwitchUtil.checkGpsIsOpen(this, "需要打开定位功能才能查看定位结果信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检查当前设备是否已经开启了定位功能
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授予定位权限并开启定位功能", Toast.LENGTH_SHORT).show();
            return;
        }
        // 从系统服务中获取定位管理器
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 获取最后一次成功定位的位置信息（network表示网络定位方式）
        Location location = mgr.getLastKnownLocation("network");
        getLocationText(location); // 获取定位结果文本
        // 获取最后一次成功定位的位置信息（gps表示卫星定位方式）
        location = mgr.getLastKnownLocation("gps");
        getLocationText(location); // 获取定位结果文本
    }

    // 获取定位结果文本
    private void getLocationText(Location location) {
        if (location != null) {
            mLocation = location;
            refreshLocationInfo(""); // 刷新定位信息
            GetAddressTask task = new GetAddressTask(); // 创建一个详细地址查询的异步任务
            task.setOnAddressListener(this); // 设置详细地址查询的监听器
            task.execute(location); // 把详细地址查询任务加入到处理队列
        }
    }

    // 在找到详细地址后触发
    @Override
    public void onFindAddress(String address) {
        refreshLocationInfo(address); // 刷新定位信息
    }

    // 刷新定位信息
    private void refreshLocationInfo(String address) {
        String desc = String.format("定位类型=%s\n定位对象信息如下： " +
                        "\n\t其中时间：%s" + "\n\t其中经度：%f，纬度：%f" +
                        "\n\t其中高度：%d米，精度：%d米" + "\n\t其中地址：%s",
                mLocation.getProvider(), DateUtil.formatDate(mLocation.getTime()),
                mLocation.getLongitude(), mLocation.getLatitude(),
                Math.round(mLocation.getAltitude()), Math.round(mLocation.getAccuracy()), address);
        tv_location.setText(desc);
    }

}
