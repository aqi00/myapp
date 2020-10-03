package com.example.chapter13;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.bean.EvaluateInfo;
import com.example.chapter13.bean.EvaluatePhoto;
import com.example.chapter13.bean.GoodsOrder;
import com.example.chapter13.database.EvaluateInfoHelper;
import com.example.chapter13.database.EvaluatePhotoHelper;
import com.example.chapter13.database.GoodsOrderHelper;
import com.example.chapter13.util.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EvaluateDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_time;
    private RatingBar rb_score;
    private TextView tv_comment;
    private LinearLayout ll_photo;
    private ImageView iv_first;
    private ImageView iv_second;
    private ImageView iv_third;
    private long evaluate_id; // 评价编号
    private EvaluateInfo mEvaluate; // 评价信息
    private List<EvaluatePhoto> mPhotoList = new ArrayList<EvaluatePhoto>(); // 图片列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_detail);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("评价详情");
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_option.setVisibility(View.VISIBLE);
        tv_option.setText("删除评价");
        tv_option.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_time = findViewById(R.id.tv_time);
        rb_score = findViewById(R.id.rb_score);
        tv_comment = findViewById(R.id.tv_comment);
        ll_photo = findViewById(R.id.ll_photo);
        iv_first = findViewById(R.id.iv_first);
        iv_second = findViewById(R.id.iv_second);
        iv_third = findViewById(R.id.iv_third);
        iv_first.setOnClickListener(this);
        iv_second.setOnClickListener(this);
        iv_third.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        evaluate_id = getIntent().getLongExtra("evaluate_id", -1);
        EvaluateInfoHelper infoHelper = EvaluateInfoHelper.getInstance(this);
        // 查询指定评价编号的评价记录
        List<EvaluateInfo> infoList = (List<EvaluateInfo>) infoHelper.queryByRowid(evaluate_id);
        if (infoList.size() > 0) {
            mEvaluate = infoList.get(0);
            tv_name.setText(mEvaluate.goods_name);
            tv_time.setText(DateUtil.convertDateString(mEvaluate.create_time));
            rb_score.setRating(mEvaluate.evaluate_star);
            tv_comment.setText(mEvaluate.evaluate_content);
        }
        EvaluatePhotoHelper photoHelper = EvaluatePhotoHelper.getInstance(this);
        // 查询指定评价编号的评价图片
        mPhotoList = photoHelper.queryByEvaluateId(evaluate_id);
        for (int i=0; i<mPhotoList.size(); i++) {
            ll_photo.setVisibility(View.VISIBLE);
            EvaluatePhoto photo = mPhotoList.get(i);
            showImage(photo.image_path, i); // 显示评价图片
        }
    }

    // 显示评价图片
    private void showImage(String image_path, int pos) {
        ImageView iv = null;
        if (pos == 0) {
            iv = iv_first;
        } else if (pos == 1) {
            iv = iv_second;
        } else if (pos == 2) {
            iv = iv_third;
        }
        if (iv != null) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageURI(Uri.parse(image_path)); // 设置图像视图的图片路径
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        } else if (v.getId() == R.id.tv_option) {
            deleteEvaluate(); // 删除当前评价
        } else if (v.getId() == R.id.iv_first) {
            openWholePhoto(0); // 打开整张图片
        } else if (v.getId() == R.id.iv_second) {
            openWholePhoto(1); // 打开整张图片
        } else if (v.getId() == R.id.iv_third) {
            openWholePhoto(1); // 打开整张图片
        }
    }

    // 删除当前评价
    private void deleteEvaluate() {
        updateEvaluateStatus(); // 更新评价状态（商品订单表的评价状态改为未评价）
        deleteEvaluateRecord(); // 删除评价记录
        finish(); // 关闭当前页面
    }

    // 更新评价状态（商品记录表的评价状态改为未评价）
    private void updateEvaluateStatus() {
        GoodsOrderHelper orderHelper = GoodsOrderHelper.getInstance(this);
        List<GoodsOrder> orderList = (List<GoodsOrder>) orderHelper.queryByRowid(mEvaluate.order_id);
        if (orderList.size() > 0) {
            GoodsOrder order = orderList.get(0);
            order.evaluate_status = 0;
            orderHelper.updateStatus(order);
        }
    }

    // 删除评价记录
    private void deleteEvaluateRecord() {
        EvaluateInfoHelper infoHelper = EvaluateInfoHelper.getInstance(this);
        infoHelper.deleteByRowid(evaluate_id);
        EvaluatePhotoHelper photoHelper = EvaluatePhotoHelper.getInstance(this);
        photoHelper.deleteByEvaluateId(evaluate_id);
        for (EvaluatePhoto photo : mPhotoList) {
            File file = new File(photo.image_path);
            file.delete(); // 删除存储卡上的图片文件
        }
        Toast.makeText(this, "已删除当前评价", Toast.LENGTH_SHORT).show();
    }

    // 打开整张图片
    private void openWholePhoto(int pos) {
        String image_path = mPhotoList.get(pos).image_path;
        // 下面跳到指定图片的浏览页面
        Intent intent = new Intent(this, EvaluatePhotoActivity.class);
        intent.putExtra("image_path", image_path); // 往意图中保存图片路径
        startActivity(intent); // 打开评价大图的页面
    }

}
