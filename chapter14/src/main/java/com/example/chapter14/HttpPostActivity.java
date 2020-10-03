package com.example.chapter14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapter14.bean.PackageInfo;
import com.example.chapter14.constant.ApkConstant;
import com.example.chapter14.task.CheckUpdateTask;
import com.example.chapter14.task.req.CheckUpdateReq;
import com.example.chapter14.task.resp.CheckUpdateResp;
import com.google.gson.Gson;

public class HttpPostActivity extends AppCompatActivity implements CheckUpdateTask.OnCheckUpdateListener {
    private static final String TAG = "HttpPostActivity";
    private Spinner sp_app_name; // 应用名称的下拉框
    private ImageView iv_app;
    private TextView tv_app_result;
    private boolean isFirstSelect = true; // 是否首次选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_post);
        iv_app = findViewById(R.id.iv_app);
        tv_app_result = findViewById(R.id.tv_app_result);
        initAppSpinner(); // 初始化应用名称的下拉框
    }

    // 初始化应用名称的下拉框
    private void initAppSpinner() {
        ArrayAdapter<String> apkNameAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, ApkConstant.NAME_ARRAY);
        sp_app_name = findViewById(R.id.sp_app_name);
        sp_app_name.setPrompt("请选择要更新的应用");
        sp_app_name.setAdapter(apkNameAdapter);
        sp_app_name.setOnItemSelectedListener(new AppNameSelectedListener());
        sp_app_name.setSelection(0);
    }

    class AppNameSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (isFirstSelect) { // 刚打开页面时不需要执行下载动作
                isFirstSelect = false;
                return;
            }
            queryAppInfo(arg2); // 查询应用的详细信息
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 查询应用的详细信息
    private void queryAppInfo(int pos) {
        iv_app.setImageResource(ApkConstant.ICON_ARRAY[pos]); // 设置图像视图的资源图片
        CheckUpdateReq req = new CheckUpdateReq(); // 创建检查更新的请求对象
        req.package_list.add(new PackageInfo(ApkConstant.PACKAGE_ARRAY[pos]));
        String content = new Gson().toJson(req); // 把检查更新的请求对象转换为json字符串
        CheckUpdateTask task = new CheckUpdateTask(); // 创建一个检查应用更新的异步任务
        task.setCheckUpdateListener(this); // 设置应用更新检查的监听器
        task.execute(content); // 把应用更新检查任务加入到处理队列
    }

    // 在结束应用更新检查时触发
    @Override
    public void finishCheckUpdate(String resp) {
        if (TextUtils.isEmpty(resp)) {
            Toast.makeText(this, "应用检查更新失败", Toast.LENGTH_SHORT).show();
            return;
        }
        // 把JSON串转换为对应结构的实体对象
        CheckUpdateResp checkResp = new Gson().fromJson(resp, CheckUpdateResp.class);
        if (checkResp!=null && checkResp.package_list!=null && checkResp.package_list.size()>0) {
            PackageInfo info = checkResp.package_list.get(0);
            String desc = String.format("应用检查更新结果如下：\n应用名称：%s\n应用包名：%s\n最新版本：%s\n下载地址：%s",
                    info.app_name, info.package_name, info.new_version, info.download_url);
            tv_app_result.setText(desc); // 显示当前选中应用的检查更新结果
        }
    }

}
