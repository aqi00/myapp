package com.example.chapter15;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_app_version).setOnClickListener(this);
        findViewById(R.id.btn_log_debug).setOnClickListener(this);
        findViewById(R.id.btn_sqlite_write).setOnClickListener(this);
        findViewById(R.id.btn_sqlite_read).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_app_version) {
            Intent intent = new Intent(this, AppVersionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_log_debug) {
            Intent intent = new Intent(this, LogDebugActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sqlite_write) {
            Intent intent = new Intent(this, SQLiteWriteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sqlite_read) {
            Intent intent = new Intent(this, SQLiteReadActivity.class);
            startActivity(intent);
        }
    }

}
