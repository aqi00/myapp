package com.example.chapter11.task;

import android.os.AsyncTask;

// 模拟异步处理的线程
public class BookLoadTask extends AsyncTask<String, Integer, String> {
    private String mBook; // 书籍名称

    public BookLoadTask(String title) {
        super();
        mBook = title;
    }

    // 线程正在后台处理
    protected String doInBackground(String... params) {
        int ratio = 0; // 下载比例
        for (; ratio <= 100; ratio += 5) {
            try {
                Thread.sleep(200); // 睡眠200毫秒模拟网络文件下载耗时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(ratio); // 通报处理进展。调用该方法会触发onProgressUpdate方法
        }
        return params[0]; // 返回参数是书籍的名称
    }

    // 准备启动线程
    protected void onPreExecute() {
        mListener.onBegin(mBook); // 触发监听器的开始事件
    }

    // 线程在通报处理进展
    protected void onProgressUpdate(Integer... values) {
        mListener.onUpdate(mBook, values[0]); // 触发监听器的进度更新事件
    }

    // 线程已经完成处理
    protected void onPostExecute(String result) {
        mListener.onFinish(result); // 触发监听器的结束事件
    }

    // 线程已经取消
    protected void onCancelled(String result) {
        mListener.onCancel(result); // 触发监听器的取消事件
    }

    private OnProgressListener mListener; // 声明一个进度更新的监听器对象
    // 设置进度更新的监听器
    public void setOnProgressListener(OnProgressListener listener) {
        mListener = listener;
    }

    // 定义一个进度更新的监听器接口
    public interface OnProgressListener {
        void onBegin(String book); // 在线程处理开始时触发
        void onUpdate(String book, int progress); // 在线程处理过程中更新进度时触发
        void onFinish(String book); // 在线程处理结束时触发
        void onCancel(String book); // 在线程处理取消时触发
    }

}
