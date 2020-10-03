package com.example.chapter14.task;

import android.os.AsyncTask;

import com.example.chapter14.util.HttpUtil;
import com.example.chapter14.constant.UrlConstant;

//获取旅游照片的异步任务
public class GetPhotoTask extends AsyncTask<Void, Void, String> {
    private final static String TAG = "GetPhotoTask";

    // 线程正在后台处理
    protected String doInBackground(Void... params) {
        // 发送HTTP请求信息，并获得HTTP应答内容。获取图片的接口地址见UrlConstant.java
        String resp = HttpUtil.get(UrlConstant.GET_PHOTO_URL, null);
        return resp;
    }

    // 线程已经完成处理
    protected void onPostExecute(String resp) {
        mListener.onGetPhoto(resp); // HTTP调用完毕，触发监听器的获取图片事件
    }

    private GetPhotoTask.GetPhotoListener mListener; // 声明一个获取图片的监听器对象
    // 设置获取图片的监听器
    public void setGetPhotoListener(GetPhotoTask.GetPhotoListener listener) {
        mListener = listener;
    }

    // 定义一个获取图片的监听器接口
    public interface GetPhotoListener {
        void onGetPhoto(String resp);
    }

}
