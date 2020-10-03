package com.example.chapter14.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chapter14.constant.UrlConstant;
import com.example.chapter14.util.HttpUtil;

// 上传文件的异步任务
public class UploadTask extends AsyncTask<String, Void, String> {
    private final static String TAG = "UploadTask";
//    public UploadTask() {
//        super();
//    }

    // 线程正在后台处理
    protected String doInBackground(String... params) {
        String filePath = params[0]; // 待上传的文件路径
        Log.d(TAG, "filePath=" + filePath);
        // 向服务地址上传指定文件。文件上传的服务地址见UrlConstant.java
        String resp = HttpUtil.upload(UrlConstant.UPLOAD_URL, filePath, null);
        Log.d(TAG, "upload result=" + resp);
        return resp; // 返回文件上传的结果
    }

    // 线程已经完成处理
    protected void onPostExecute(String result) {
        mListener.finishUpload(result); // HTTP上传完毕，触发监听器的上传结束事件
    }

    private OnUploadListener mListener; // 声明一个文件上传的监听器对象
    // 设置文件上传的监听器
    public void setOnUploadListener(OnUploadListener listener) {
        mListener = listener;
    }

    // 定义一个文件上传的监听器接口
    public interface OnUploadListener {
        void finishUpload(String result);
    }

}
