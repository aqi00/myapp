package com.example.chapter14.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.chapter14.constant.UrlConstant;
import com.example.chapter14.util.HttpUtil;
import com.example.chapter14.util.FileUtil;
import com.example.chapter14.util.DateUtil;

// 获取图片验证码的异步任务
public class GetImageCodeTask extends AsyncTask<Void, Void, String> {
    private final static String TAG = "GetImageCodeTask";
    private Context mContext; // 声明一个上下文对象

    public GetImageCodeTask(Context ctx) {
        super();
        mContext = ctx;
    }

    // 线程正在后台处理
    protected String doInBackground(Void... params) {
        // 为验证码地址添加一个随机时间串。图片验证码的网址见UrlConstant.java
        String url = UrlConstant.IMAGE_CODE_URL + DateUtil.getNowDateTime();
        Log.d(TAG, "image url=" + url);
        // 获得验证码图片的临时保存路径
        String filePath = String.format("%s/%s.jpg",
                mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "verify_"+ DateUtil.getNowDateTime());
        Bitmap bitmap = HttpUtil.getImage(url, null); // 访问网络地址获得位图对象
        FileUtil.saveImage(filePath, bitmap); // 把HTTP获得的位图数据保存为图片
        Log.d(TAG, "image path=" + filePath);
        return filePath; // 返回验证码图片的本地路径
    }

    // 线程已经完成处理
    protected void onPostExecute(String path) {
        mListener.onGetCode(path); // HTTP调用完毕，触发监听器的得到验证码事件
    }

    private OnImageCodeListener mListener; // 声明一个获取图片验证码的监听器对象
    // 设置获取图片验证码的监听器
    public void setOnImageCodeListener(OnImageCodeListener listener) {
        mListener = listener;
    }

    // 定义一个获取图片验证码的监听器接口
    public interface OnImageCodeListener {
        void onGetCode(String path);
    }

}
