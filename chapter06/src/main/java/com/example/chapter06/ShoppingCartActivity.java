package com.example.chapter06;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter06.bean.CartInfo;
import com.example.chapter06.bean.GoodsInfo;
import com.example.chapter06.database.CartDBHelper;
import com.example.chapter06.database.GoodsDBHelper;
import com.example.chapter06.util.FileUtil;
import com.example.chapter06.util.SharedUtil;
import com.example.chapter06.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SetTextI18n")
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ShoppingCartActivity";
    private TextView tv_count;
    private TextView tv_total_price;
    private LinearLayout ll_content;
    private LinearLayout ll_cart; // 声明一个购物车列表的线性布局对象
    private LinearLayout ll_empty;
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("购物车");
        tv_count = findViewById(R.id.tv_count);
        tv_total_price = findViewById(R.id.tv_total_price);
        ll_content = findViewById(R.id.ll_content);
        ll_cart = findViewById(R.id.ll_cart);
        ll_empty = findViewById(R.id.ll_empty);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_shopping_channel).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_settle).setOnClickListener(this);
    }

    // 显示购物车图标中的商品数量
    private void showCount() {
        tv_count.setText("" + MainApplication.goodsCount);
        if (MainApplication.goodsCount == 0) {
            ll_content.setVisibility(View.GONE);
            ll_cart.removeAllViews(); // 移除下面的所有子视图
            mGoodsMap.clear();
            ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_content.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) { // 点击了返回图标
            finish(); // 关闭当前页面
        } else if (v.getId() == R.id.btn_shopping_channel) { // 点击了“商场”按钮
            // 从购物车页面跳到商场页面
            Intent intent = new Intent(this, ShoppingChannelActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent); // 跳转到手机商场页面
        } else if (v.getId() == R.id.btn_clear) { // 点击了“清空”按钮
            mCartHelper.deleteAll(); // 清空购物车数据库
            MainApplication.goodsCount = 0;
            showCount(); // 显示最新的商品数量
            ToastUtil.show(this, "购物车已清空");
        } else if (v.getId() == R.id.btn_settle) { // 点击了“结算”按钮
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("结算商品");
            builder.setMessage("客官抱歉，支付功能尚未开通，请下次再来");
            builder.setPositiveButton("我知道了", null);
            builder.create().show(); // 显示提醒对话框
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCount(); // 显示购物车的商品数量
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(this, 1);
        mGoodsHelper.openWriteLink(); // 打开商品数据库的写连接
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(this, 1);
        mCartHelper.openWriteLink(); // 打开购物车数据库的写连接
        downloadGoods(); // 模拟从网络下载商品图片
        showCart(); // 展示购物车中的商品列表
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoodsHelper.closeLink(); // 关闭商品数据库的数据库连接
        mCartHelper.closeLink(); // 关闭购物车数据库的数据库连接
    }

    // 声明一个购物车中的商品信息列表
    private List<CartInfo> mCartArray = new ArrayList<CartInfo>();
    // 声明一个根据商品编号查找商品信息的映射
    private HashMap<Long, GoodsInfo> mGoodsMap = new HashMap<Long, GoodsInfo>();

    private void deleteGoods(CartInfo info) {
        MainApplication.goodsCount -= info.count;
        // 从购物车的数据库中删除商品
        mCartHelper.delete("goods_id=" + info.goods_id);
        // 从购物车的列表中删除商品
        for (int i = 0; i < mCartArray.size(); i++) {
            if (info.goods_id == mCartArray.get(i).goods_id) {
                mCartArray.remove(i);
                break;
            }
        }
        showCount(); // 显示最新的商品数量
        ToastUtil.show(this, "已从购物车删除" + mGoodsMap.get(info.goods_id).name);
        mGoodsMap.remove(info.goods_id);
        refreshTotalPrice(); // 刷新购物车中所有商品的总金额
    }

    // 展示购物车中的商品列表
    private void showCart() {
        ll_cart.removeAllViews(); // 移除下面的所有子视图
        mCartArray = mCartHelper.query("1=1"); // 查询购物车数据库中所有的商品记录
        Log.d(TAG, "mCartArray.size()=" + mCartArray.size());
        if (mCartArray == null || mCartArray.size() <= 0) {
            return;
        }
        for (int i = 0; i < mCartArray.size(); i++) {
            final CartInfo info = mCartArray.get(i);
            // 根据商品编号查询商品数据库中的商品记录
            final GoodsInfo goods = mGoodsHelper.queryById(info.goods_id);
            Log.d(TAG, "name=" + goods.name + ",price=" + goods.price + ",desc=" + goods.desc);
            mGoodsMap.put(info.goods_id, goods);
            // 获取布局文件item_goods.xml的根视图
            View view = LayoutInflater.from(this).inflate(R.layout.item_cart, null);
            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_desc = view.findViewById(R.id.tv_desc);
            TextView tv_count = view.findViewById(R.id.tv_count);
            TextView tv_price = view.findViewById(R.id.tv_price);
            TextView tv_sum = view.findViewById(R.id.tv_sum);
            // 给商品行添加点击事件。点击商品行跳到商品的详情页
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShoppingCartActivity.this, ShoppingDetailActivity.class);
                    intent.putExtra("goods_id", info.goods_id);
                    startActivity(intent); // 跳到商品详情页面
                }
            });
            // 给商品行添加长按事件。长按商品行就删除该商品
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
                    builder.setMessage("是否从购物车删除"+goods.name+"？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ll_cart.removeView(v); // 移除当前视图
                            deleteGoods(info); // 删除该商品
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.create().show(); // 显示提醒对话框
                    return true;
                }
            });
            iv_thumb.setImageURI(Uri.parse(goods.pic_path)); // 设置商品图片
            tv_name.setText(goods.name); // 设置商品名称
            tv_desc.setText(goods.desc); // 设置商品描述
            tv_count.setText("" + info.count); // 设置商品数量
            tv_price.setText("" + (int)goods.price); // 设置商品单价
            tv_sum.setText("" + (int)(info.count * goods.price)); // 设置商品总价
            ll_cart.addView(view); // 往购物车列表添加该商品行
        }
        refreshTotalPrice(); // 重新计算购物车中的商品总金额
    }

    // 重新计算购物车中的商品总金额
    private void refreshTotalPrice() {
        int total_price = 0;
        for (CartInfo info : mCartArray) {
            GoodsInfo goods = mGoodsMap.get(info.goods_id);
            total_price += goods.price * info.count;
        }
        tv_total_price.setText("" + total_price);
    }

    private String mFirst = "true"; // 是否首次打开
    // 模拟网络数据，初始化数据库中的商品信息
    private void downloadGoods() {
        // 获取共享参数保存的是否首次打开参数
        mFirst = SharedUtil.getIntance(this).readString("first", "true");
        // 获取当前App的私有下载路径
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        if (mFirst.equals("true")) { // 如果是首次打开
            ArrayList<GoodsInfo> goodsList = GoodsInfo.getDefaultList(); // 模拟网络图片下载
            for (int i = 0; i < goodsList.size(); i++) {
                GoodsInfo info = goodsList.get(i);
                long rowid = mGoodsHelper.insert(info); // 往商品数据库插入一条该商品的记录
                info.rowid = rowid;
                Bitmap pic = BitmapFactory.decodeResource(getResources(), info.pic);
                String pic_path = path + rowid + ".jpg";
                FileUtil.saveImage(pic_path, pic); // 往存储卡保存商品图片
                pic.recycle(); // 回收位图对象
                info.pic_path = pic_path;
                mGoodsHelper.update(info); // 更新商品数据库中该商品记录的图片路径
            }
        }
        // 把是否首次打开写入共享参数
        SharedUtil.getIntance(this).writeString("first", "false");
    }

}
