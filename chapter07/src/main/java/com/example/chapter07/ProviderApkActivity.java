package com.example.chapter07;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.chapter07.bean.ApkInfo;
import com.example.chapter07.util.FileUtil;
import com.example.chapter07.util.ToastUtil;
import com.example.chapter07.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProviderApkActivity extends AppCompatActivity {
    private final static String TAG = "ProviderApkActivity";
    private TextView tv_title;
    private LinearLayout ll_list; // 安装包列表的线性布局
    private List<ApkInfo> mApkList = new ArrayList<ApkInfo>(); // 安装包列表
    private Uri mFilesUri = MediaStore.Files.getContentUri("external"); // 存储卡的Uri
    private String[] mFilesColumn = new String[]{ // 媒体库的字段名称数组
            MediaStore.Files.FileColumns._ID, // 编号
            MediaStore.Files.FileColumns.TITLE, // 标题
            MediaStore.Files.FileColumns.SIZE, // 文件大小
            MediaStore.Files.FileColumns.DATA, // 文件路径
            MediaStore.Files.FileColumns.MIME_TYPE}; // 媒体类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_apk);
        tv_title = findViewById(R.id.tv_title);
        ll_list = findViewById(R.id.ll_list);
        loadApkList(); // 加载安装包列表
        if (mApkList.size() == 0) {
            tv_title.setText("未找到任何apk文件");
        } else {
            showApkList(); // 显示安装包列表
        }
    }

    // 加载安装包列表
    private void loadApkList() {
        mApkList.clear(); // 清空安装包列表
        // 查找存储卡上所有的apk文件，其中mime_type指定了APK的文件类型，或者判断文件路径是否以.apk结尾
        Cursor cursor = getContentResolver().query(mFilesUri, mFilesColumn,
                "mime_type='application/vnd.android.package-archive' or _data like '%.apk'", null, null);
        if (cursor != null) {
            // 下面遍历结果集，并逐个添加到安装包列表。简单起见只挑选前十个文件
            for (int i=0; i<10 && cursor.moveToNext(); i++) {
                ApkInfo apk = new ApkInfo(); // 创建一个安装包信息对象
                apk.setId(cursor.getLong(0)); // 设置安装包编号
                apk.setName(cursor.getString(1)); // 设置安装包名称
                apk.setSize(cursor.getLong(2)); // 设置安装包的文件大小
                apk.setPath(cursor.getString(3)); // 设置安装包的文件路径
                Log.d(TAG, apk.getName() + ", " + apk.getSize() + ", " + apk.getPath()+", "+cursor.getString(4));
                if (!FileUtil.checkFileUri(this, apk.getPath())) { // 检查该路径是否合法
                    i--;
                    continue; // 路径非法则再来一次
                }
                mApkList.add(apk); // 添加至安装包列表
            }
            cursor.close(); // 关闭数据库游标
        }
    }

    // 显示APK文件列表
    private void showApkList() {
        for (int  i=0; i<mApkList.size(); i++) {
            final ApkInfo apkInfo = mApkList.get(i);
            String desc = String.format("%s，文件大小%d", apkInfo.getName(), apkInfo.getSize());
            TextView tv_apk = new TextView(this); // 创建一个文本视图
            tv_apk.setText(desc);
            tv_apk.setTextColor(Color.BLACK);
            tv_apk.setTextSize(17);
            int pad = Utils.dip2px(this, 5);
            tv_apk.setPadding(pad, pad, 0, 0); // 设置文本视图的内部间距
            final int pos = i;
            tv_apk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(apkInfo); // 显示安装apk的提示对话框
                }
            });
            ll_list.addView(tv_apk); // 把文本视图添加至安装包列表的线性布局
        }
    }

    // 显示安装apk的提示对话框
    private void showAlert(final ApkInfo apkInfo) {
        PackageManager pm = getPackageManager(); // 获取应用包管理器
        // 获取apk文件的包信息
        PackageInfo pi = pm.getPackageArchiveInfo(apkInfo.getPath(), PackageManager.GET_ACTIVITIES);
        if (pi != null) { // 能找到包信息
            Log.d(TAG, "packageName="+pi.packageName+", versionName="+pi.versionName+", versionCode="+pi.versionCode);
            String desc = String.format("应用包名：%s\n版本名称：%s\n版本编码：%s\n文件路径：%s",
                    pi.packageName, pi.versionName, pi.versionCode, apkInfo.getPath());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("是否安装该应用？"); // 设置提醒对话框的标题
            builder.setMessage(desc); // 设置提醒对话框的消息内容
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    installApk(apkInfo.getPath()); // 安装指定路径的APK
                }
            });
            builder.setNegativeButton("否", null);
            builder.create().show(); // 显示提醒对话框
        } else { // 未找到包信息
            ToastUtil.show(this, "该安装包已经损坏，请选择其他安装包");
        }
    }

    // 安装指定路径的APK
    private void installApk(String path) {
        Log.d(TAG, "path="+path);
        Uri uri = Uri.parse(path); // 根据指定路径创建一个Uri对象
        // 兼容Android7.0，把访问文件的Uri方式改为FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 通过FileProvider获得安装包文件的Uri访问方式
            uri = FileProvider.getUriForFile(this,
                    getPackageName()+".fileProvider", new File(path));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW); // 创建一个浏览动作的意图
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 另外开启新页面
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 需要读权限
        // 设置Uri的数据类型为APK文件
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent); // 启动系统自带的应用安装程序
    }

}
