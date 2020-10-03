package com.example.chapter11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_notify_simple).setOnClickListener(this);
        findViewById(R.id.btn_notify_counter).setOnClickListener(this);
        findViewById(R.id.btn_notify_channel).setOnClickListener(this);
        findViewById(R.id.btn_notify_marker).setOnClickListener(this);
        findViewById(R.id.btn_service_normal).setOnClickListener(this);
        findViewById(R.id.btn_bind_immediate).setOnClickListener(this);
        findViewById(R.id.btn_bind_delay).setOnClickListener(this);
        findViewById(R.id.btn_foreground_service).setOnClickListener(this);
        findViewById(R.id.btn_handler_message).setOnClickListener(this);
        findViewById(R.id.btn_async_task).setOnClickListener(this);
        findViewById(R.id.btn_intent_service).setOnClickListener(this);
        findViewById(R.id.btn_notify_remind).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_notify_simple) {
            Intent intent = new Intent(this, NotifySimpleActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_counter) {
            Intent intent = new Intent(this, NotifyCounterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_channel) {
            Intent intent = new Intent(this, NotifyChannelActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_marker) {
            Intent intent = new Intent(this, NotifyMarkerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_service_normal) {
            Intent intent = new Intent(this, ServiceNormalActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bind_immediate) {
            Intent intent = new Intent(this, BindImmediateActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_bind_delay) {
            Intent intent = new Intent(this, BindDelayActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_foreground_service) {
            Intent intent = new Intent(this, ForegroundServiceActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_handler_message) {
            Intent intent = new Intent(this, HandlerMessageActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_async_task) {
            Intent intent = new Intent(this, AsyncTaskActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_intent_service) {
            Intent intent = new Intent(this, IntentServiceActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_notify_remind) {
            Intent intent = new Intent(this, NotifyRemindActivity.class);
            startActivity(intent);
        }
    }

}
