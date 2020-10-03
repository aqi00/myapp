package com.example.chapter14.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.chapter14.PhotoDetailActivity;
import com.example.chapter14.R;
import com.example.chapter14.bean.PhotoInfo;
import com.example.chapter14.util.Utils;
import com.example.chapter14.widget.RecyclerExtras.OnItemClickListener;

import java.util.List;
import java.util.Random;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> implements OnItemClickListener {
    private final static String TAG = "PhotoRecyclerAdapter";
    private Context mContext; // 声明一个上下文对象
    private List<PhotoInfo> mPhotoList; // 照片列表

    public PhotoRecyclerAdapter(Context context, List<PhotoInfo> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    public void setPhotoList(List<PhotoInfo> photoList) {
        mPhotoList = photoList;
    }

    // 获取列表项的个数
    public int getItemCount() {
        return mPhotoList.size();
    }

    // 创建列表项的视图持有者
    public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
        // 根据布局文件item_photo.xml生成视图对象
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_photo, vg, false);
        return new ItemHolder(v);
    }

    // 绑定列表项的视图持有者
    public void onBindViewHolder(ViewHolder vh, final int position) {
        ItemHolder holder = (ItemHolder) vh;
        PhotoInfo photo = mPhotoList.get(position);
        ViewGroup.LayoutParams params = holder.ll_item.getLayoutParams();
        params.height = 150 + new Random().nextInt(100); // 生成随机高度，从而呈现瀑布流效果
        params.height = Utils.dip2px(mContext, params.height);
        holder.ll_item.setLayoutParams(params);
        holder.tv_title.setText(photo.title);
        // 利用Glide加载网络图片，并在图像视图上显示
        Glide.with(mContext).load(photo.image_url)
                .transition(DrawableTransitionOptions.withCrossFade(1000)) // 设置时长1秒的渐变动画
                .into(holder.iv_pic);
        // 列表项的点击事件需要自己实现
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    // 获取列表项的类型
    public int getItemViewType(int position) {
        return 0;
    }

    // 获取列表项的编号
    public long getItemId(int position) {
        return position;
    }

    // 定义列表项的视图持有者
    public class ItemHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_item; // 声明列表项的线性布局
        public ImageView iv_pic; // 声明一个照片的图像视图
        public TextView tv_title; // 声明一个标题的文本视图

        public ItemHolder(View v) {
            super(v);
            ll_item = v.findViewById(R.id.ll_item);
            iv_pic = v.findViewById(R.id.iv_pic);
            tv_title = v.findViewById(R.id.tv_title);
        }
    }

    // 声明列表项的点击监听器对象
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 处理列表项的点击事件
    public void onItemClick(View view, int position) {
        PhotoInfo photo = mPhotoList.get(position);
        // 以下跳到照片详情页面
        Intent intent = new Intent(mContext, PhotoDetailActivity.class);
        intent.putExtra("title", photo.title);
        intent.putExtra("image_url", photo.image_url);
        mContext.startActivity(intent); // 打开照片详情页面
    }

}
