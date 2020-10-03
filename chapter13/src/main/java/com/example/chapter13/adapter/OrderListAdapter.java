package com.example.chapter13.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chapter13.EvaluateDetailActivity;
import com.example.chapter13.EvaluateGoodsActivity;
import com.example.chapter13.R;
import com.example.chapter13.bean.EvaluateInfo;
import com.example.chapter13.bean.GoodsOrder;
import com.example.chapter13.database.EvaluateInfoHelper;
import com.example.chapter13.util.DateUtil;

import java.util.List;

@SuppressLint("SetTextI18n")
public class OrderListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private Context mContext; // 声明一个上下文对象
    private List<GoodsOrder> mOrderList; // 声明一个订单信息列表

    // 订单适配器的构造方法，传入上下文、订单里的商品列表
    public OrderListAdapter(Context context, List<GoodsOrder> order_list) {
        mContext = context;
        mOrderList = order_list;
    }

    // 获取列表项的个数
    public int getCount() {
        return mOrderList.size();
    }

    // 获取列表项的数据
    public Object getItem(int arg0) {
        return mOrderList.get(arg0);
    }

    // 获取列表项的编号
    public long getItemId(int arg0) {
        return arg0;
    }

    // 获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) { // 转换视图为空
            holder = new ViewHolder(); // 创建一个新的视图持有者
            // 根据布局文件item_order.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, null);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder); // 将视图持有者保存到转换视图当中
        } else { // 转换视图非空
            // 从转换视图中获取之前保存的视图持有者
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsOrder order = mOrderList.get(position);
        holder.tv_time.setText(DateUtil.convertDateString(order.create_time)); // 显示订单时间
        holder.tv_name.setText(order.goods_name); // 显示商品名称
        holder.tv_price.setText("" + (int) order.price); // 显示商品价格
        if (order.evaluate_status == 0) { // 尚未评价
            holder.tv_status.setText("未评价"); // 显示评价状态
            holder.tv_status.setTextColor(Color.RED);
        } else { // 已经评价
            holder.tv_status.setText("已评价"); // 显示评价状态
            holder.tv_status.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GoodsOrder order = mOrderList.get(position);
        if (order.evaluate_status == 0) { // 未评价
            Intent intent = new Intent(mContext, EvaluateGoodsActivity.class);
            intent.putExtra("order_id", order.rowid);
            mContext.startActivity(intent); // 跳到评价商品页面
        } else { // 已评价
            EvaluateInfoHelper helper = EvaluateInfoHelper.getInstance(mContext);
            EvaluateInfo evaluate = helper.queryByOrderId(order.rowid);
            Intent intent = new Intent(mContext, EvaluateDetailActivity.class);
            intent.putExtra("evaluate_id", evaluate.rowid);
            mContext.startActivity(intent); // 跳到评价详情页面
        }
    }

    // 定义一个视图持有者，以便重用列表项的视图资源
    public final class ViewHolder {
        public TextView tv_time; // 声明订单时间的文本视图对象
        public TextView tv_name; // 声明商品名称的文本视图对象
        public TextView tv_price; // 声明商品价格的文本视图对象
        public TextView tv_status; // 声明评价状态的文本视图对象
    }

}
