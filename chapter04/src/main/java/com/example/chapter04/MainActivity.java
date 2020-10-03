package com.example.chapter04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_act_open).setOnClickListener(this);
        findViewById(R.id.btn_act_life).setOnClickListener(this);
        findViewById(R.id.btn_act_jump).setOnClickListener(this);
        findViewById(R.id.btn_act_login).setOnClickListener(this);
        findViewById(R.id.btn_action_uri).setOnClickListener(this);
        findViewById(R.id.btn_send_receive).setOnClickListener(this);
        findViewById(R.id.btn_request_response).setOnClickListener(this);
        findViewById(R.id.btn_read_string).setOnClickListener(this);
        findViewById(R.id.btn_meta_data).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_act_open) {
            Intent intent = new Intent(this, ActStartActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_act_life) {
            Intent intent = new Intent(this, ActLifeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_act_jump) {
            Intent intent = new Intent(this, JumpFirstActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_act_login) {
            Intent intent = new Intent(this, LoginInputActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_action_uri) {
            Intent intent = new Intent(this, ActionUriActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_send_receive) {
            Intent intent = new Intent(this, ActSendActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_request_response) {
            Intent intent = new Intent(this, ActRequestActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_read_string) {
            Intent intent = new Intent(this, ReadStringActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_meta_data) {
            Intent intent = new Intent(this, MetaDataActivity.class);
            startActivity(intent);
        }
    }

}
