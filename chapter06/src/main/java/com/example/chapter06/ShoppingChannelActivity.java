package com.example.chapter06;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter06.bean.GoodsInfo;
import com.example.chapter06.database.CartDBHelper;
import com.example.chapter06.database.GoodsDBHelper;
import com.example.chapter06.util.ToastUtil;
import com.example.chapter06.util.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class ShoppingChannelActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_count;
    private GridLayout gl_channel; // 声明一个商品频道的网格布局对象
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_channel);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("手机商场");
        tv_count = findViewById(R.id.tv_count);
        gl_channel = findViewById(R.id.gl_channel);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_cart).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) { // 点击了返回图标
            finish(); // 关闭当前页面
        } else if (v.getId() == R.id.iv_cart) { // 点击了购物车图标
            // 从商场页面跳到购物车页面
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent); // 跳转到购物车页面
        }
    }

    // 把指定编号的商品添加到购物车
    private void addToCart(long goods_id, String goods_name) {
        MainApplication.goodsCount++;
        tv_count.setText("" + MainApplication.goodsCount);
        mCartHelper.save(goods_id); // 把该商品填入购物车数据库
        ToastUtil.show(this, "已添加一部" + goods_name + "到购物车");
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_count.setText("" + MainApplication.goodsCount);
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(this, 1);
        mGoodsHelper.openReadLink(); // 打开商品数据库的读连接
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(this, 1);
        mCartHelper.openWriteLink(); // 打开购物车数据库的写连接
        showGoods(); // 展示商品列表
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoodsHelper.closeLink(); // 关闭商品数据库的数据库连接
        mCartHelper.closeLink(); // 关闭购物车数据库的数据库连接
    }

    private void showGoods() {
        int screenWidth = Utils.getScreenWidth(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                screenWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        gl_channel.removeAllViews(); // 移除下面的所有子视图
        // 查询商品数据库中的所有商品记录
        List<GoodsInfo> goodsArray = mGoodsHelper.query("1=1");
        for (final GoodsInfo info : goodsArray) {
            // 获取布局文件item_goods.xml的根视图
            View view = LayoutInflater.from(this).inflate(R.layout.item_goods, null);
            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_price = view.findViewById(R.id.tv_price);
            Button btn_add = view.findViewById(R.id.btn_add);
            tv_name.setText(info.name); // 设置商品名称
            iv_thumb.setImageURI(Uri.parse(info.pic_path)); // 设置商品图片
            iv_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShoppingChannelActivity.this, ShoppingDetailActivity.class);
                    intent.putExtra("goods_id", info.rowid);
                    startActivity(intent); // 跳到商品详情页面
                }
            });
            tv_price.setText("" + (int)info.price); // 设置商品价格
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(info.rowid, info.name); // 添加到购物车
                }
            });
            gl_channel.addView(view, params); // 把商品视图添加到网格布局
        }
    }

}
