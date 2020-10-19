package com.example.chapter14;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter14.widget.TextProgressCircle;

import java.util.HashMap;

@SuppressLint("DefaultLocale")
public class DownloadImageActivity extends AppCompatActivity {
    private Spinner sp_image_url; // 图片链接的下拉框
    private ImageView iv_image_url;
    private TextProgressCircle tpc_progress; // 定义一个文本进度圈对象
    private TextView tv_image_result;
    private boolean isFirstSelect = true; // 是否首次选择
    private Uri mImageUri; // 图片的路径对象
    private DownloadManager mDownloadManager; // 声明一个下载管理器对象
    private long mDownloadId = 0; // 当前任务的下载编号
    private static HashMap<Integer, String> mStatusMap = new HashMap<Integer, String>(); // 下载状态映射
    static { // 初始化下载状态映射
        mStatusMap.put(DownloadManager.STATUS_PENDING, "挂起");
        mStatusMap.put(DownloadManager.STATUS_RUNNING, "运行中");
        mStatusMap.put(DownloadManager.STATUS_PAUSED, "暂停");
        mStatusMap.put(DownloadManager.STATUS_SUCCESSFUL, "成功");
        mStatusMap.put(DownloadManager.STATUS_FAILED, "失败");
    }
    private String[] imageDescArray = {
            "洱海公园", "丹凤亭", "宛在堂", "满庭芳", "玉带桥", "眺望洱海", "洱海女儿", "海心亭", "洱海岸边", "烟波浩渺"
    };
    private String[] imageUrlArray = {
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/nYJcslMIrGeDrujE5KZF2xBW8rjXMIVetZfrOAlSamM!/b/dPwxB5iaEQAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/Adcl9XVS.RBED4D8shjceYHOhhR*6mcNyCcq24kJG2k!/b/dPwxB5iYEQAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/bg*X6nT03YUReoJ97ked266WlWG3IzLjBdwHpKqkhYY!/b/dOg5CpjZEAAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/JOPAKl9BO1wragCEIVzXLlHwj83qVhb8uNuHdmVRwP4!/b/dPwxB5iSEQAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/7hHOgBEOBshH*7YAUx7RP0JzPuxRBD727mblw9TObhc!/b/dG4WB5i2EgAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/m4Rjx20D9iFL0D5emuYqMMDji*HGQ2w2BWqv0zK*tRk!/b/dGp**5dYEAAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/swfCMVl7Oefv8xgboV3OqkrahEs33KO7XwwH6hh7bnY!/b/dECE*5e9EgAA",
            "https://b256.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/tpRlB0oozaD9PyBtCmf3pQ5QY0keJJxYGX93I7n5NwQ!/b/dAyVmZiVEQAA",
            "https://b256.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/wMX2*LM6y.mBsFIYu8spAa7xXWUkPD.GHyazd.vMmYA!/b/dGYwoZjREQAA",
            "https://b255.photo.store.qq.com/psb?/V11ZojBI0Zz6pV/2vl1n0KmKTPCv944MVJgLxKAhMiM*sqajIFQ43c*9DM!/b/dPaoCJhuEQAA",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);
        iv_image_url = findViewById(R.id.iv_image_url);
        // 从布局文件中获取名叫tpc_progress的文本进度圈
        tpc_progress = findViewById(R.id.tpc_progress);
        tv_image_result = findViewById(R.id.tv_image_result);
        // 从系统服务中获取下载管理器
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        initImageSpinner(); // 初始化下载图片的下拉框
    }

    // 初始化下载图片的下拉框
    private void initImageSpinner() {
        ArrayAdapter<String> imageUrlAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, imageDescArray);
        sp_image_url = findViewById(R.id.sp_image_url);
        sp_image_url.setPrompt("请选择要下载的图片");
        sp_image_url.setAdapter(imageUrlAdapter);
        sp_image_url.setOnItemSelectedListener(new ImageUrlSelectedListener());
        sp_image_url.setSelection(0);
    }

    class ImageUrlSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isFirstSelect) { // 刚打开页面时不需要执行下载动作
                isFirstSelect = false;
                return;
            }
            startDownload(arg2); // 开始下载指定序号的图片文件
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 开始下载指定序号的图片文件
    private void startDownload(int pos) {
        iv_image_url.setImageDrawable(null); // 清空图像视图
        tpc_progress.setProgress(0); // 设置文本进度圈的当前进度为0，最大进度为100
        tpc_progress.setVisibility(View.VISIBLE); // 显示文本进度圈
        Uri uri = Uri.parse(imageUrlArray[pos]); // 根据图片的下载地址构建一个路径对象
        Request down = new Request(uri); // 创建一个下载请求对象，指定从哪里下载文件
        // 设置允许下载的网络类型
        down.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
        down.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 设置不在通知栏显示
        // 设置下载文件在本地的保存路径
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DCIM, pos + ".jpg");
        mDownloadId = mDownloadManager.enqueue(down); // 把下载请求对象加入到下载队列
        mHandler.post(mRefresh); // 启动下载进度的刷新任务
    }

    private Handler mHandler = new Handler(Looper.myLooper()); // 声明一个处理器对象
    // 定义一个下载进度的刷新任务
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            boolean isFinish = false;
            Query down_query = new Query(); // 创建一个下载查询对象，按照下载编号过滤
            down_query.setFilterById(mDownloadId); // 设置下载查询对象的编号过滤器
            // 向下载管理器查询下载任务，并返回查询结果集的游标
            Cursor cursor = mDownloadManager.query(down_query);
            while (cursor.moveToNext()) {
                int uriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                int mediaIdx = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE);
                int totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int nowIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (cursor.getString(uriIdx) == null) {
                    break;
                }
                // 根据总大小和已下载大小，计算当前的下载进度
                int progress = (int) (100 * cursor.getLong(nowIdx) / cursor.getLong(totalIdx));
                tpc_progress.setProgress(progress); // 设置文本进度圈的当前进度
                if (progress == 100) { // 下载完毕
                    isFinish = true;
                }
                // 获得实际的下载状态
                int status = isFinish ? DownloadManager.STATUS_SUCCESSFUL : cursor.getInt(statusIdx);
                mImageUri = Uri.parse(cursor.getString(uriIdx));
                String desc = String.format("文件路径：%s\n媒体类型：%s\n文件总大小：%d字节" +
                                "\n已下载大小：%d字节\n下载进度：%d%%\n下载状态：%s",
                        mImageUri.toString(), cursor.getString(mediaIdx), cursor.getLong(totalIdx),
                        cursor.getLong(nowIdx), progress, mStatusMap.get(status));
                tv_image_result.setText(desc); // 显示图片下载任务的下载详情
            }
            cursor.close(); // 关闭数据库游标
            if (!isFinish) { // 下载未完成，则继续刷新
                mHandler.postDelayed(this, 50); // 延迟50毫秒后再次启动刷新任务
            } else { // 下载已完成，则显示图片
                tpc_progress.setVisibility(View.INVISIBLE); // 隐藏文本进度圈
                iv_image_url.setImageURI(mImageUri); // 设置图像视图的图片路径
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRefresh); // 移除刷新任务
    }
}
