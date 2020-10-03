package com.example.chapter13;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter13.util.BitmapUtil;
import com.example.chapter13.util.DateUtil;

public class PhotoTakeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "PhotoTakeActivity";
    private int THUMBNAIL_CODE = 1; // 获取缩略图的请求码
    private int ORIGINAL_CODE = 2; // 获取原始图的请求码
    private ImageView iv_photo; // 声明一个图像视图对象
    private Uri mImageUri; // 图片的路径对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_take);
        iv_photo = findViewById(R.id.iv_photo);
        findViewById(R.id.btn_thumbnail).setOnClickListener(this);
        findViewById(R.id.btn_original).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_thumbnail) {
            // 下面通过系统相机拍照只能获得缩略图
            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoIntent, THUMBNAIL_CODE); // 打开系统相机
        } else if (v.getId() == R.id.btn_original) {
            takeOriginalPhoto(); // 拍照时获取原始图片
        }
    }

    // 拍照时获取原始图片
    private void takeOriginalPhoto() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Android10开始必须由系统自动分配路径，同时该方式也能自动刷新相册
        ContentValues values = new ContentValues();
        // 指定图片文件的名称
        values.put(MediaStore.Video.Media.DISPLAY_NAME, "photo_"+DateUtil.getNowDateTime());
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg"); // 类型为图像
        // 通过内容解析器插入一条外部内容的路径信息
        mImageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { // 不推荐使用以下代码，因为不会自动刷新相册
//                // 获得图片的临时保存路径
//                String filePath = String.format("%s/%s.jpg",
//                        getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo_"+ DateUtil.getNowDateTime());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0以上要通过FileProvider转换
//                    mImageUri = FileProvider.getUriForFile(this, getString(R.string.file_provider), new File(filePath));
//                } else { // 7.0以下直接根据路径生成对应的Uri
//                    mImageUri = Uri.parse(filePath);
//                }
//            }
        // 下面通过系统相机拍照可以获得原始图
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(photoIntent, ORIGINAL_CODE); // 打开系统相机
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode==RESULT_OK && requestCode==THUMBNAIL_CODE){ // 获得缩略图
            // 缩略图放在返回意图中的data字段，将其取出转成位图对象即可
            Bundle extras = intent.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(bitmap); // 设置图像视图的位图对象
            Log.d(TAG, "getWidth="+bitmap.getWidth()+", getHeight="+bitmap.getHeight());
        }
        if (resultCode==RESULT_OK && requestCode==ORIGINAL_CODE) { // 获得原始图
            //iv_photo.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 设置图层类型为软件加速
            //iv_photo.setImageURI(mImageUri); // 设置图像视图的路径对象
            // 需要自动缩小原始图片，因为过大的图片无法显示，会报下列错误：
            // Bitmap too large to be uploaded into a texture (3120x4208, max=4096x4096)
            // 根据指定图片的uri，获得自动缩小后的位图对象
            Bitmap bitmap = BitmapUtil.getAutoZoomImage(this, mImageUri);
            iv_photo.setImageBitmap(bitmap); // 设置图像视图的位图对象
            Log.d(TAG, "getWidth="+bitmap.getWidth()+", getHeight="+bitmap.getHeight());
        }
    }

}
