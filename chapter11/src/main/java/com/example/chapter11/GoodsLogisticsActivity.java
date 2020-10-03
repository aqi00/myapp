package com.example.chapter11;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter11.util.DateUtil;
import com.example.chapter11.util.NotifyUtil;

import java.util.Random;

public class GoodsLogisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_waybill;
    private TextView tv_time;
    private TextView tv_logistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_logistics);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("物流详情");
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_waybill = findViewById(R.id.tv_waybill);
        tv_time = findViewById(R.id.tv_time);
        tv_logistics = findViewById(R.id.tv_logistics);
        showLogisticsInfo(); // 显示物流信息
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        }
    }

    // 显示物流信息
    private void showLogisticsInfo() {
        String goods_name = getIntent().getStringExtra("goods_name");
        tv_name.setText(goods_name); // 设置商品名称
        String waybill_id = String.format("%08d", new Random().nextInt(30000000));
        tv_waybill.setText(waybill_id); // 设置物流编号
        String arrive_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
        tv_time.setText(arrive_time); // 设置运达时间
        String logistics_info = String.format("您购买的商品已于%s送达**省**市**区**街道**小区快递柜，请凭取件码%s及时领取您的包裹。",
                arrive_time, String.format("%06d", new Random().nextInt(999999)));
        tv_logistics.setText(logistics_info); // 设置物流详情
        NotifyUtil.showMarkerCount(this, 0, null); // 消息数量为0，则不显示消息角标
    }

}
