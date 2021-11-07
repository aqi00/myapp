package com.example.chapter07;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter07.util.PermissionUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_content_write).setOnClickListener(this);
        findViewById(R.id.btn_content_read).setOnClickListener(this);
        findViewById(R.id.btn_file_write).setOnClickListener(this);
        findViewById(R.id.btn_file_read).setOnClickListener(this);
        findViewById(R.id.btn_contact_add).setOnClickListener(this);
        findViewById(R.id.btn_contact_read).setOnClickListener(this);
        findViewById(R.id.btn_monitor_sms).setOnClickListener(this);
        findViewById(R.id.btn_send_mms).setOnClickListener(this);
        findViewById(R.id.btn_provider_mms).setOnClickListener(this);
        findViewById(R.id.btn_provider_apk).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_content_write) {
            Intent intent = new Intent(this, ContentWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_content_read) {
            Intent intent = new Intent(this, ContentReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_file_write) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, R.id.btn_file_write % 65536)) {
                startActivity(new Intent(this, FileWriteActivity.class));
            }
        } else if (v.getId() == R.id.btn_file_read) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, R.id.btn_file_read % 65536)) {
                startActivity(new Intent(this, FileReadActivity.class));
            }
        } else if (v.getId() == R.id.btn_contact_add) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_CONTACTS, R.id.btn_contact_add % 65536)) {
                startActivity(new Intent(this, ContactAddActivity.class));
            }
        } else if (v.getId() == R.id.btn_contact_read) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.READ_CONTACTS, R.id.btn_contact_read % 65536)) {
                startActivity(new Intent(this, ContactReadActivity.class));
            }
        } else if (v.getId() == R.id.btn_monitor_sms) {
            if (PermissionUtil.checkPermission(this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, R.id.btn_monitor_sms % 65536)) {
                startActivity(new Intent(this, MonitorSmsActivity.class));
            }
        } else if (v.getId() == R.id.btn_send_mms) {
            Intent intent = new Intent(this, SendMmsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_provider_mms) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, R.id.btn_provider_mms % 65536)) {
                startActivity(new Intent(this, ProviderMmsActivity.class));
            }
        } else if (v.getId() == R.id.btn_provider_apk) {
            if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, R.id.btn_provider_apk % 65536)) {
                startActivity(new Intent(this, ProviderApkActivity.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode不能为负数，也不能大于2的16次方即65536
        if (requestCode == R.id.btn_file_write % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, FileWriteActivity.class));
            } else {
                //ToastUtil.show(this, "需要允许存储卡权限才能写入公共空间噢");
                Toast.makeText(this, "需要允许存储卡权限才能写入公共空间噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_file_read % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, FileReadActivity.class));
            } else {
                Toast.makeText(this, "需要允许存储卡权限才能读取公共空间噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_contact_add % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, ContactAddActivity.class));
            } else {
                Toast.makeText(this, "需要允许通讯录权限才能读写联系人噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_contact_read % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, ContactReadActivity.class));
            } else {
                Toast.makeText(this, "需要允许通讯录权限才能读写联系人噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_monitor_sms % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, MonitorSmsActivity.class));
            } else {
                Toast.makeText(this, "需要允许短信权限才能校准流量噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_provider_mms % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, ProviderMmsActivity.class));
            } else {
                Toast.makeText(this, "需要允许存储卡权限才能发送彩信噢", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == R.id.btn_provider_apk % 65536) {
            if (PermissionUtil.checkGrant(grantResults)) { // 用户选择了同意授权
                startActivity(new Intent(this, ProviderApkActivity.class));
            } else {
                Toast.makeText(this, "需要允许存储卡权限才能安装应用噢", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
