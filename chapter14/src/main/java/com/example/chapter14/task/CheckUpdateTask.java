package com.example.chapter14.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chapter14.constant.UrlConstant;
import com.example.chapter14.util.HttpUtil;

// 检查应用更新的异步任务
public class CheckUpdateTask extends AsyncTask<String, Void, String> {
    private final static String TAG = "CheckUpdateTask";
//    public CheckUpdateTask() {
//        super();
//    }

    // 线程正在后台处理
    protected String doInBackground(String... params) {
        String req = params[0]; // HTTP请求内容
        Log.d(TAG, "req = " + req);
        // 发送HTTP请求信息，并获得HTTP应答内容。检查更新的接口地址见UrlConstant.java
        String resp = HttpUtil.post(UrlConstant.CHECK_UPDATE_URL, req, null);
        Log.d(TAG, "resp = " + resp);
        return resp; // 返回HTTP应答内容
    }

    // 线程已经完成处理
    protected void onPostExecute(String resp) {
        mListener.finishCheckUpdate(resp); // HTTP调用完毕，触发监听器的结束检查事件
    }

    private OnCheckUpdateListener mListener; // 声明一个结束更新检查的监听器对象
    // 设置结束更新检查的监听器
    public void setCheckUpdateListener(OnCheckUpdateListener listener) {
        mListener = listener;
    }

    // 定义一个结束更新检查的监听器接口
    public interface OnCheckUpdateListener {
        void finishCheckUpdate(String resp);
    }

}
