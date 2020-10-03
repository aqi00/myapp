package com.example.chapter06;

import android.app.Application;
import android.util.Log;

import com.example.chapter06.database.CartDBHelper;

import java.util.HashMap;

public class MainApplication extends Application {
    private final static String TAG = "MainApplication";
    private static MainApplication mApp; // 声明一个当前应用的静态实例
    // 声明一个公共的信息映射对象，可当作全局变量使用
    public HashMap<String, String> infoMap = new HashMap<String, String>();
    public static int goodsCount = 0;

    // 利用单例模式获取当前应用的唯一实例
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mApp = this; // 在打开应用时对静态的应用实例赋值
        // 获取购物车数据库的帮助器对象
        CartDBHelper cartHelper = CartDBHelper.getInstance(this, 1);
        cartHelper.openReadLink(); // 打开购物车数据库的读连接
        goodsCount = cartHelper.queryCount(); // 查询购物车里面的商品数量
        cartHelper.closeLink(); // 关闭购物车数据库的读连接
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate");
    }

}
