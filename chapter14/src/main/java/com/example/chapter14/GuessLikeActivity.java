package com.example.chapter14;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.example.chapter14.adapter.PhotoRecyclerAdapter;
import com.example.chapter14.bean.PhotoInfo;
import com.example.chapter14.task.resp.GetPhotoResp;
import com.example.chapter14.task.GetPhotoTask;
import com.example.chapter14.widget.SpacesDecoration;
import com.google.gson.Gson;

import java.util.List;

@SuppressLint("DefaultLocale")
public class GuessLikeActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener, GetPhotoTask.GetPhotoListener {
    private final static String TAG = "GuessLikeActivity";
    private SwipeRefreshLayout srl_like; // 声明一个下拉刷新布局对象
    private RecyclerView rv_like; // 声明一个循环视图对象
    private PhotoRecyclerAdapter mAdapter; // 声明一个线性适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_like);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("大美河山");
        findViewById(R.id.iv_back).setOnClickListener(this);
        initRecyclerView(); // 初始化瀑布流布局的循环视图
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        }
    }

    // 初始化瀑布流布局的循环视图
    private void initRecyclerView() {
        rv_like = findViewById(R.id.rv_like); // 从布局文件中获取名叫rv_like的循环视图
        // 创建一个垂直方向的瀑布流网格布局管理器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        rv_like.setLayoutManager(manager); // 设置循环视图的布局管理器
        rv_like.addItemDecoration(new SpacesDecoration(1)); // 设置循环视图的空白装饰
        srl_like = findViewById(R.id.srl_like); // 从布局文件中获取名叫srl_like的下拉刷新布局
        srl_like.setOnRefreshListener(this); // 设置下拉刷新布局的下拉刷新监听器
        // 设置下拉刷新布局的进度圆圈颜色
        srl_like.setColorSchemeResources(R.color.red, R.color.orange, R.color.green, R.color.blue);
        srl_like.setRefreshing(true); // 设置状态为正在刷新，此时会弹出进度圆圈
        onRefresh(); // 执行刷新动作
    }

    // 一旦在下拉刷新布局内部往下拉动页面，就触发下拉监听器的onRefresh方法
    @Override
    public void onRefresh() {
        GetPhotoTask task = new GetPhotoTask(); // 创建一个获取照片的异步任务
        task.setGetPhotoListener(this); // 设置照片获取的监听器
        task.execute(); // 把照片获取任务加入到处理队列
    }

    // 在获得照片列表信息后触发
    @Override
    public void onGetPhoto(String resp) {
        srl_like.setRefreshing(false); // 设置状态为正在刷新，此时会关闭进度圆圈
        // 把JSON串转换为对应结构的实体对象
        GetPhotoResp photoResp = new Gson().fromJson(resp, GetPhotoResp.class);
        if (photoResp == null) { // 未获得返回报文，说明HTTP调用失败
            return;
        }
        List<PhotoInfo> photo_list = photoResp.getPhotoList();
        if (photo_list!=null && photo_list.size()>0) {
            Log.d(TAG, "photo_list.size()="+photo_list.size());
            if (mAdapter == null) { // 首次加载前不存在适配器
                // 构建一个照片列表的瀑布流网格适配器
                mAdapter = new PhotoRecyclerAdapter(this, photo_list);
                mAdapter.setOnItemClickListener(mAdapter); // 设置照片列表的点击监听器
                rv_like.setAdapter(mAdapter); // 设置循环视图的瀑布流网格适配器
            } else { // 再次加载时已经存在适配器了
                mAdapter.setPhotoList(photo_list);
                mAdapter.notifyDataSetChanged(); // 通知适配器发生了数据变更
            }
            rv_like.scrollToPosition(0); // 让循环视图滚动到第一项所在的位置
        }
    }
}
