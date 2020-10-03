package com.example.chapter07;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter07.util.ToastUtil;
import com.example.chapter07.util.ViewUtil;

public class SendMmsActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "SendMmsActivity";
    private EditText et_phone;
    private EditText et_title;
    private EditText et_message;
    private ImageView iv_appendix;
    private Uri mUri; // 文件的路径对象
    private int CHOOSE_CODE = 3; // 选择照片的请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mms);
        et_phone = findViewById(R.id.et_phone);
        et_title = findViewById(R.id.et_title);
        et_message = findViewById(R.id.et_message);
        iv_appendix = findViewById(R.id.iv_appendix);
        iv_appendix.setOnClickListener(this);
        findViewById(R.id.btn_send_mms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_appendix) {
            ViewUtil.hideOneInputMethod(this, et_message); // 隐藏输入法软键盘
            // 创建一个内容获取动作的意图
            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            albumIntent.setType("image/*"); // 设置内容类型为图像
            startActivityForResult(albumIntent, CHOOSE_CODE); // 打开系统相册，并等待图片选择结果
        } else if (v.getId() == R.id.btn_send_mms) {
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
            if (mUri == null) {
                ToastUtil.show(this, "请选择附件图片");
                return;
            }
            // 发送带图片的彩信
            sendMms(et_phone.getText().toString(), et_title.getText().toString(), et_message.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) { // 从相册选择一张照片
            if (intent.getData() != null) { // 数据非空，表示选中了某张照片
                mUri = intent.getData(); // 获得选中照片的路径对象
                iv_appendix.setImageURI(mUri); // 设置图像视图的路径对象
                Log.d(TAG, "uri.getPath="+mUri.getPath()+",uri.toString="+mUri.toString());
            }
        }
    }

    // 发送带图片的彩信
    private void sendMms(String phone, String title, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 创建一个发送动作的意图
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 另外开启新页面
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 需要读权限
        intent.putExtra("address", phone); // 彩信发送的目标号码
        intent.putExtra("subject", title); // 彩信的标题
        intent.putExtra("sms_body", message); // 彩信的内容
        intent.putExtra(Intent.EXTRA_STREAM, mUri); // mUri为彩信的图片附件
        intent.setType("image/*"); // 彩信的附件为图片
        // 部分手机无法直接跳到彩信发送页面，故而需要用户手动选择彩信应用
        //intent.setClassName("com.android.mms","com.android.mms.ui.ComposeMessageActivity");
        startActivity(intent); // 因为未指定要打开哪个页面，所以系统会在底部弹出选择窗口
        ToastUtil.show(this, "请在弹窗中选择短信或者信息应用");
    }

}
