package com.example.chapter14;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter14.util.PermissionUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_json_convert).setOnClickListener(this);
        findViewById(R.id.btn_http_get).setOnClickListener(this);
        findViewById(R.id.btn_http_post).setOnClickListener(this);
        findViewById(R.id.btn_download_apk).setOnClickListener(this);
        findViewById(R.id.btn_download_image).setOnClickListener(this);
        findViewById(R.id.btn_http_upload).setOnClickListener(this);
        findViewById(R.id.btn_http_image).setOnClickListener(this);
        findViewById(R.id.btn_glide_simple).setOnClickListener(this);
        findViewById(R.id.btn_glide_cache).setOnClickListener(this);
        findViewById(R.id.btn_guess_like).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_json_convert) {
            Intent intent = new Intent(this, JsonConvertActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_http_get) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, R.id.btn_http_get % 65536)) {
                startActivity(new Intent(this, HttpGetActivity.class));
            }
        } else if (v.getId() == R.id.btn_http_post) {
            Intent intent = new Intent(this, HttpPostActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_download_apk) {
            Intent intent = new Intent(this, DownloadApkActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_download_image) {
            Intent intent = new Intent(this, DownloadImageActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_http_upload) {
            Intent intent = new Intent(this, HttpUploadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_http_image) {
            Intent intent = new Intent(this, HttpImageActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_glide_simple) {
            Intent intent = new Intent(this, GlideSimpleActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_glide_cache) {
            Intent intent = new Intent(this, GlideCacheActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_guess_like) {
            Intent intent = new Intent(this, GuessLikeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == R.id.btn_http_get % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, HttpGetActivity.class));
            } else {
                Toast.makeText(this, "需要允许定位权限才能开始定位噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_http_upload % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) {
                startActivity(new Intent(this, HttpUploadActivity.class));
            } else {
                Toast.makeText(this, "需要允许存储卡权限才能上传文件噢", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
