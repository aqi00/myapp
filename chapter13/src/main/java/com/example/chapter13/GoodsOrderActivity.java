package com.example.chapter13;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.adapter.OrderListAdapter;
import com.example.chapter13.bean.GoodsOrder;
import com.example.chapter13.database.GoodsOrderHelper;
import com.example.chapter13.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "GoodsOrderActivity";
    private ListView lv_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_order);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("订单列表");
        findViewById(R.id.iv_back).setOnClickListener(this);
        lv_order = findViewById(R.id.lv_order);
        importOrder(); // 导入商品订单
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initOrderList(); // 初始化订单列表
    }

    // 导入商品订单
    private void importOrder() {
        if (SharedUtil.getIntance(this).readInt("import_flag", 0) == 0) {
            List<GoodsOrder> order_list = GoodsOrder.getDefaultList();
            GoodsOrderHelper helper = new GoodsOrderHelper(this);
            helper.insert(order_list); // 往数据库写入商品订单
            SharedUtil.getIntance(this).writeInt("import_flag", 1);
        }
        Log.d(TAG, "import_flag="+SharedUtil.getIntance(this).readInt("import_flag", 0));
    }

    // 初始化订单列表
    private void initOrderList() {
        GoodsOrderHelper helper = new GoodsOrderHelper(this);
        // 从数据库中查询商品记录
        List<GoodsOrder> order_list = (ArrayList<GoodsOrder>) helper.queryAll();
        OrderListAdapter adapter = new OrderListAdapter(this, order_list);
        lv_order.setAdapter(adapter); // 设置列表视图的适配器
        lv_order.setOnItemClickListener(adapter); // 设置列表视图的点击监听器
    }

}
