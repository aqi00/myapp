package com.example.chapter11;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter11.service.RemindIntentService;
import com.example.chapter11.util.ViewUtil;

public class NotifyRemindActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_goods;
    private EditText et_delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_remind);
        et_goods = findViewById(R.id.et_goods);
        et_delay = findViewById(R.id.et_delay);
        findViewById(R.id.btn_remind).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_remind) {
            ViewUtil.hideOneInputMethod(this, et_goods); // 隐藏输入法软键盘
            if (TextUtils.isEmpty(et_goods.getText())) {
                Toast.makeText(this, "请填写商品名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(et_delay.getText())) {
                Toast.makeText(this, "请填写延迟时间", Toast.LENGTH_SHORT).show();
                return;
            }
            // 创建一个通往物流提醒服务的意图
            Intent intent = new Intent(this, RemindIntentService.class);
            intent.putExtra("goods_name", et_goods.getText().toString());
            intent.putExtra("delay_time", Integer.parseInt(et_delay.getText().toString()));
            startService(intent); // 启动物流提醒服务
            Toast.makeText(this, "已启动物流提醒服务，请注意观察是否收到提醒通知", Toast.LENGTH_SHORT).show();
        }
    }

}
