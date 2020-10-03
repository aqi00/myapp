package com.example.chapter09;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_broad_standard).setOnClickListener(this);
        findViewById(R.id.btn_broad_order).setOnClickListener(this);
        findViewById(R.id.btn_broad_static).setOnClickListener(this);
        findViewById(R.id.btn_system_minute).setOnClickListener(this);
        findViewById(R.id.btn_system_network).setOnClickListener(this);
        findViewById(R.id.btn_alarm).setOnClickListener(this);
        findViewById(R.id.btn_act_test).setOnClickListener(this);
        findViewById(R.id.btn_change_direction).setOnClickListener(this);
        findViewById(R.id.btn_return_desktop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_broad_standard) {
            Intent intent = new Intent(this, BroadStandardActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_broad_order) {
            Intent intent = new Intent(this, BroadOrderActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_broad_static) {
            Intent intent = new Intent(this, BroadStaticActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_system_minute) {
            Intent intent = new Intent(this, SystemMinuteActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_system_network) {
            Intent intent = new Intent(this, SystemNetworkActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_alarm) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_act_test) {
            Intent intent = new Intent(this, ActTestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_change_direction) {
            Intent intent = new Intent(this, ChangeDirectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_return_desktop) {
            Intent intent = new Intent(this, ReturnDesktopActivity.class);
            startActivity(intent);
        }
    }

}
