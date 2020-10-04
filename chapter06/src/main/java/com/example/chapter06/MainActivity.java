package com.example.chapter06;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_share_write).setOnClickListener(this);
        findViewById(R.id.btn_share_read).setOnClickListener(this);
        findViewById(R.id.btn_login_share).setOnClickListener(this);
        findViewById(R.id.btn_sqlite_create).setOnClickListener(this);
        findViewById(R.id.btn_sqlite_write).setOnClickListener(this);
        findViewById(R.id.btn_sqlite_read).setOnClickListener(this);
        findViewById(R.id.btn_login_sqlite).setOnClickListener(this);
        findViewById(R.id.btn_file_path).setOnClickListener(this);
        findViewById(R.id.btn_file_write).setOnClickListener(this);
        findViewById(R.id.btn_file_read).setOnClickListener(this);
        findViewById(R.id.btn_image_write).setOnClickListener(this);
        findViewById(R.id.btn_image_read).setOnClickListener(this);
        findViewById(R.id.btn_app_life).setOnClickListener(this);
        findViewById(R.id.btn_app_write).setOnClickListener(this);
        findViewById(R.id.btn_app_read).setOnClickListener(this);
        findViewById(R.id.btn_room_write).setOnClickListener(this);
        findViewById(R.id.btn_room_read).setOnClickListener(this);
        findViewById(R.id.btn_shopping_cart).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_share_write) {
            Intent intent = new Intent(this, ShareWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_share_read) {
            Intent intent = new Intent(this, ShareReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login_share) {
            Intent intent = new Intent(this, LoginShareActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sqlite_create) {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sqlite_write) {
            Intent intent = new Intent(this, SQLiteWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sqlite_read) {
            Intent intent = new Intent(this, SQLiteReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login_sqlite) {
            Intent intent = new Intent(this, LoginSQLiteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_file_path) {
            Intent intent = new Intent(this, FilePathActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_file_write) {
            Intent intent = new Intent(this, FileWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_file_read) {
            Intent intent = new Intent(this, FileReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_image_write) {
            Intent intent = new Intent(this, ImageWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_image_read) {
            Intent intent = new Intent(this, ImageReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_app_life) {
            Intent intent = new Intent(this, ActTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_app_write) {
            Intent intent = new Intent(this, AppWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_app_read) {
            Intent intent = new Intent(this, AppReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_room_write) {
            Intent intent = new Intent(this, RoomWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_room_read) {
            Intent intent = new Intent(this, RoomReadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_shopping_cart) {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            startActivity(intent);
        }
    }

}
