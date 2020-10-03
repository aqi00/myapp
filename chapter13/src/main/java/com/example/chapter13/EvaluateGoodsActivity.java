package com.example.chapter13;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.chapter13.util.BitmapUtil;
import com.example.chapter13.util.DateUtil;
import com.example.chapter13.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class EvaluateGoodsActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "EvaluateGoodsActivity";
    private int COMBINE_CODE = 4; // 既可拍照获得现场图片、也可在相册挑选已有图片的请求码
    private TextView tv_hint;
    private RatingBar rb_score;
    private EditText et_comment;
    private ImageView iv_first;
    private ImageView iv_second;
    private ImageView iv_third;
    private ImageView iv_current;
    private GoodsOrder mOrder;
    private Uri mImageUri; // 图片的路径对象
    private List<String> mImageList = new ArrayList<String>(); // 图片文件的路径列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_goods);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("评价商品");
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_hint = findViewById(R.id.tv_hint);
        rb_score = findViewById(R.id.rb_score);
        et_comment = findViewById(R.id.et_comment);
        iv_first = findViewById(R.id.iv_first);
        iv_second = findViewById(R.id.iv_second);
        iv_third = findViewById(R.id.iv_third);
        iv_first.setOnClickListener(this);
        iv_second.setOnClickListener(this);
        iv_third.setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        getGoodsOrder(); // 获取商品订单
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish(); // 关闭当前页面
        } else if (v.getId() == R.id.iv_first) {
            openSelectDialog(v); // 打开选择对话框（要拍照还是去相册）
        } else if (v.getId() == R.id.iv_second) {
            openSelectDialog(v); // 打开选择对话框（要拍照还是去相册）
        } else if (v.getId() == R.id.iv_third) {
            openSelectDialog(v); // 打开选择对话框（要拍照还是去相册）
        } else if (v.getId() == R.id.btn_commit) {
            if (TextUtils.isEmpty(et_comment.getText())) {
                Toast.makeText(this, "请填写评价内容", Toast.LENGTH_SHORT).show();
                return;
            }
            commitEvaluate(); // 提交评价
        }
    }

    // 获取商品订单
    private void getGoodsOrder() {
        long order_id = getIntent().getLongExtra("order_id", -1);
        GoodsOrderHelper mOrderHelper = GoodsOrderHelper.getInstance(this);
        // 获取指定订单编号的商品订单
        List<GoodsOrder> mOrderList = (List<GoodsOrder>) mOrderHelper.queryByRowid(order_id);
        if (mOrderList.size() > 0) {
            mOrder = mOrderList.get(0);
            tv_hint.setText(String.format("请填写对%s的评价：", mOrder.goods_name));
        }
    }

    // 打开选择对话框（要拍照还是去相册）
    private void openSelectDialog(View v) {
        iv_current = (ImageView) v;
        // Android10开始必须由系统自动分配路径，同时该方式也能自动刷新相册
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DISPLAY_NAME, "photo_"+ DateUtil.getNowDateTime());
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg"); // 类型为图像
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // 声明相机的拍照行为
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 往意图存入待拍摄的图片路径
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        Intent[] intentArray = new Intent[] { photoIntent };
        // 声明相册的打开行为
        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 是否允许多选
        albumIntent.setType("image/*"); // 类型为图像
        // 容纳相机和相册在内的选择意图
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "请拍照或选择图片");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, albumIntent);
        // 创建封装好标题的选择器意图
        Intent chooser = Intent.createChooser(chooserIntent, "选择图片");
        // 在页面底部弹出多种选择方式的列表对话框
        startActivityForResult(chooser, COMBINE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == COMBINE_CODE) {
            Bitmap bitmap = getPhoto(intent); // 从照片获得位图对象
            iv_current.setImageBitmap(bitmap); // 设置图像视图的位图对象
            Log.d(TAG, "bitmap.getByteCount="+bitmap.getByteCount()+", bitmap.getWidth="+bitmap.getWidth()+", bitmap.getHeight="+bitmap.getHeight());
            addBitmapList(bitmap, R.id.iv_first, 0, iv_second);
            addBitmapList(bitmap, R.id.iv_second, 1, iv_third);
            addBitmapList(bitmap, R.id.iv_third, 2, null);
        }
    }

    // 添加到位图列表
    private void addBitmapList(Bitmap bitmap, int iv_id, int pos, ImageView iv_next) {
        if (iv_current.getId() == iv_id) {
            // 获得图片的临时保存路径
            String filePath = String.format("%s/%s.jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo_"+ DateUtil.getNowDateTime());
            FileUtil.saveImage(filePath, bitmap); // 把位图保存为图片
            if (mImageList.size() <= pos) {
                mImageList.add(filePath);
            } else {
                mImageList.set(pos, filePath);
            }
            if (iv_next != null) {
                iv_next.setVisibility(View.VISIBLE);
            }
        }
    }

    // 从照片获得位图对象
    private Bitmap getPhoto(Intent intent) {
        Bitmap bitmap;
        if (intent!=null && intent.getData()!=null) { // 从相册选择一张照片
            Uri uri = intent.getData(); // 获得已选择照片的路径对象
            // 根据指定图片的uri，获得自动缩小后的位图对象
            bitmap = BitmapUtil.getAutoZoomImage(this, uri);
        } else { // 拍照的原始图片
            // 根据指定图片的uri，获得自动缩小后的位图对象
            bitmap = BitmapUtil.getAutoZoomImage(this, mImageUri);
        }
        return bitmap;
    }

    // 提交评价
    private void commitEvaluate() {
        mOrder.evaluate_status = 1;
        GoodsOrderHelper mOrderHelper = GoodsOrderHelper.getInstance(this);
        mOrderHelper.updateStatus(mOrder); // 更新该商品订单的评价状态
        saveEvaluateRecord(); // 保存评价记录
        finish(); // 关闭当前页面
    }

    // 保存评价记录
    private void saveEvaluateRecord() {
        EvaluateInfo info = new EvaluateInfo();
        info.order_id = mOrder.rowid;
        info.goods_name = mOrder.goods_name;
        info.evaluate_star = (int) rb_score.getRating();
        info.evaluate_content = et_comment.getText().toString();
        info.create_time = DateUtil.getNowDateTime();
        EvaluateInfoHelper infoHelper = EvaluateInfoHelper.getInstance(this);
        long evaluate_id = infoHelper.insert(info); // 插入评价记录
        EvaluatePhotoHelper photoHelper = EvaluatePhotoHelper.getInstance(this);
        for (String image_path : mImageList) {
            EvaluatePhoto photo = new EvaluatePhoto();
            photo.evaluate_id = evaluate_id;
            photo.image_path = image_path;
            photoHelper.insert(photo); // 插入评价图片
        }
    }

}
