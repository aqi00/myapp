package com.example.chapter13;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.chapter13.util.BitmapUtil;

import java.io.InputStream;

public class ImageChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ImageChangeActivity";
    private ImageView iv_photo; // 声明一个图像视图对象
    private Bitmap mBitmap; // 声明一个位图对象
    private Uri mImageUri; // 图片的路径对象
    private int CHOOSE_CODE = 3; // 选择照片的请求码
    private float mDegree; // 旋转角度
    private float mRatio; // 缩放比例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_change);
        iv_photo = findViewById(R.id.iv_photo);
        findViewById(R.id.btn_choose).setOnClickListener(this);
        initScaleSpinner(); // 初始化缩放比率下拉框
        initRotateSpinner(); // 初始化旋转角度下拉框
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_choose) {
            // 创建一个内容获取动作的意图（准备跳到系统相册）
            Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            albumIntent.setType("image/*"); // 设置内容类型为图像
            startActivityForResult(albumIntent, CHOOSE_CODE); // 打开系统相册
        }
    }

    // 初始化缩放比率下拉框
    private void initScaleSpinner() {
        ArrayAdapter<String> scaleAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, scaleArray);
        Spinner sp_scale = findViewById(R.id.sp_scale);
        sp_scale.setPrompt("请选择缩放比率");
        sp_scale.setAdapter(scaleAdapter);
        sp_scale.setOnItemSelectedListener(new ScaleSelectedListener());
        sp_scale.setSelection(0);
    }

    private String[] scaleArray = {"1.0", "0.5", "0.2", "0.10", "0.05", "0.01"};
    class ScaleSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            mRatio = Float.parseFloat(scaleArray[arg2]);
            showChangedImage(); // 显示变更后的图像
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 初始化旋转角度下拉框
    private void initRotateSpinner() {
        ArrayAdapter<String> rotateAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, rotateArray);
        Spinner sp_rotate = findViewById(R.id.sp_rotate);
        sp_rotate.setPrompt("请选择旋转角度");
        sp_rotate.setAdapter(rotateAdapter);
        sp_rotate.setOnItemSelectedListener(new RotateSelectedListener());
        sp_rotate.setSelection(0);
    }

    private String[] rotateArray = {"0", "45", "90", "135", "180", "225", "270", "315"};
    class RotateSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            mDegree = Integer.parseInt(rotateArray[arg2]);
            showChangedImage(); // 显示变更后的图像
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 显示变更后的图像
    private void showChangedImage() {
        if (mBitmap != null) {
            // 获得缩放后的位图对象
            Bitmap bitmap = BitmapUtil.getScaleBitmap(mBitmap, mRatio);
            // 获得旋转后的位图对象
            bitmap = BitmapUtil.getRotateBitmap(bitmap, mDegree);
            iv_photo.setImageBitmap(bitmap); // 设置图像视图的位图对象
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) {
            if (intent.getData() != null) { // 从相册选择一张照片
                mImageUri = intent.getData();
                // 打开指定uri获得输入流对象
                try (InputStream is = getContentResolver().openInputStream(mImageUri)) {
                    // 从输入流解码得到原始的位图对象
                    mBitmap = BitmapFactory.decodeStream(is);
                    iv_photo.setImageBitmap(mBitmap); // 设置图像视图的位图对象
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "uri.getPath="+mImageUri.getPath()+",uri.toString="+mImageUri.toString());
            }
        }
    }

}
