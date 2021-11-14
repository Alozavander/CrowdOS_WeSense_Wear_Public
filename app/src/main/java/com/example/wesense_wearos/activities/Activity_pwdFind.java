package com.example.wesense_wearos.activities;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wesense_wearos.R;


public class Activity_pwdFind extends WearableActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_find);

        initView();
    }

    private void initView() {
        findViewById(R.id.activity_pwdFind_next_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_pwdFind_next_bt:

                break;
        }
    }
}
