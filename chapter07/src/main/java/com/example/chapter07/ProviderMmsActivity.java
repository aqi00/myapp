package com.example.chapter07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.chapter07.bean.ImageInfo;
import com.example.chapter07.util.FileUtil;
import com.example.chapter07.util.ToastUtil;
import com.example.chapter07.util.Utils;
import com.example.chapter07.util.ViewUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProviderMmsActivity extends AppCompatActivity {
    private final static String TAG = "ProviderMmsActivity";
    private EditText et_phone;
    private EditText et_title;
    private EditText et_message;
    private GridLayout gl_appendix; // 图片附件的网格布局
    private List<ImageInfo> mImageList = new ArrayList<ImageInfo>(); // 图片列表
    private Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // 相册的Uri
    private String[] mImageColumn = new String[]{ // 媒体库的字段名称数组
            MediaStore.Images.Media._ID, // 编号
            MediaStore.Images.Media.TITLE, // 标题
            MediaStore.Images.Media.SIZE, // 文件大小
            MediaStore.Images.Media.DATA}; // 文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_mms);
        et_phone = findViewById(R.id.et_phone);
        et_title = findViewById(R.id.et_title);
        et_message = findViewById(R.id.et_message);
        gl_appendix = findViewById(R.id.gl_appendix);
        loadImageList(); // 加载图片列表
        showImageGrid(); // 显示图像网格
    }

    // 加载图片列表
    private void loadImageList() {
        mImageList.clear(); // 清空图片列表
        // 查询相册媒体库，并返回结果集的游标。“_size asc”表示按照文件大小升序排列
        Cursor cursor = getContentResolver().query(mImageUri, mImageColumn, null, null, "_size asc");
        if (cursor != null) {
            // 下面遍历结果集，并逐个添加到图片列表。简单起见只挑选前六张图片
            for (int i=0; i<6 && cursor.moveToNext(); i++) {
                ImageInfo image = new ImageInfo(); // 创建一个图片信息对象
                image.setId(cursor.getLong(0)); // 设置图片编号
                image.setName(cursor.getString(1)); // 设置图片名称
                image.setSize(cursor.getLong(2)); // 设置图片的文件大小
                image.setPath(cursor.getString(3)); // 设置图片的文件路径
                Log.d(TAG, image.getName() + " " + image.getSize() + " " + image.getPath());
                if (!FileUtil.checkFileUri(this, image.getPath())) { // 检查该路径是否合法
                    i--;
                    continue; // 路径非法则再来一次
                }
                mImageList.add(image); // 添加至图片列表
            }
            cursor.close(); // 关闭数据库游标
        }
    }

    // 显示图像网格
    private void showImageGrid() {
        for (int i=0; i<mImageList.size(); i++) {
            final ImageInfo image = mImageList.get(i);
            Bitmap bitmap = BitmapFactory.decodeFile(image.getPath()); // 从指定路径解码得到位图对象
            ImageView iv_appendix = new ImageView(this); // 创建一个图像视图
            iv_appendix.setImageBitmap(bitmap); // 设置图像视图的位图对象
            iv_appendix.setScaleType(ImageView.ScaleType.FIT_CENTER); // 设置图像视图的缩放类型
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    Utils.dip2px(this, 110), Utils.dip2px(this, 110));
            iv_appendix.setLayoutParams(params); // 设置图像视图的布局参数
            int pad = Utils.dip2px(this, 5);
            iv_appendix.setPadding(pad, pad, pad, pad); // 设置图像视图的内部间距
            iv_appendix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickImage(image); // 执行点击图片动作
                }
            });
            gl_appendix.addView(iv_appendix); // 把图像视图添加至网格布局
        }
    }

    // 执行点击图片动作
    public void clickImage(ImageInfo image) {
        if (TextUtils.isEmpty(et_phone.getText())) {
            ToastUtil.show(this, "请填写对方号码");
            return;
        }
        if (TextUtils.isEmpty(et_title.getText())) {
            ToastUtil.show(this, "请填写彩信标题");
            return;
        }
        if (TextUtils.isEmpty(et_message.getText())) {
            ToastUtil.show(this, "请填写彩信内容");
            return;
        }
        ViewUtil.hideOneInputMethod(this, et_message); // 隐藏输入法软键盘
        // 发送带图片的彩信
        sendMms(et_phone.getText().toString(), et_title.getText().toString(), et_message.getText().toString(), image.getPath());
    }

    // 发送带图片的彩信
    private void sendMms(String phone, String title, String message, String path) {
        Log.d(TAG, "path="+path);
        Uri uri = Uri.parse(path); // 根据指定路径创建一个Uri对象
        // 兼容Android7.0，把访问文件的Uri方式改为FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 通过FileProvider获得文件的Uri访问方式
            uri = FileProvider.getUriForFile(this,
                    "com.example.chapter07.fileProvider", new File(path));
        }
        Intent intent = new Intent(Intent.ACTION_SEND); // 创建一个发送动作的意图
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 另外开启新页面
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 需要读权限
        intent.putExtra("address", phone); // 彩信发送的目标号码
        intent.putExtra("subject", title); // 彩信的标题
        intent.putExtra("sms_body", message); // 彩信的内容
        intent.putExtra(Intent.EXTRA_STREAM, uri); // uri为彩信的图片附件
        intent.setType("image/*"); // 彩信的附件为图片
        // 部分手机无法直接跳到彩信发送页面，故而需要用户手动选择彩信应用
        //intent.setClassName("com.android.mms","com.android.mms.ui.ComposeMessageActivity");
        startActivity(intent); // 因为未指定要打开哪个页面，所以系统会在底部弹出选择窗口
        ToastUtil.show(this, "请在弹窗中选择短信或者信息应用");
    }

}