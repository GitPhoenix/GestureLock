package com.alley.lock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alley.lock.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnSetLock;
    private Button mBtnVerifyLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
        setUpListener();
    }

    private void setUpView() {
        mBtnSetLock = (Button) findViewById(R.id.btn_set_lockpattern);
        mBtnVerifyLock = (Button) findViewById(R.id.btn_verify_lockpattern);
    }

    private void setUpListener() {
        mBtnSetLock.setOnClickListener(this);
        mBtnVerifyLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_lockpattern:
                startSetLockPattern();
                break;

            case R.id.btn_verify_lockpattern:
                startVerifyLockPattern();
                break;

            default:
                break;
        }
    }

    private void startSetLockPattern() {
        Intent intent = new Intent(MainActivity.this, GestureSettingActivity.class);
        startActivity(intent);
    }

    private void startVerifyLockPattern() {
        Intent intent = new Intent(MainActivity.this, GestureVerifyActivity.class);
        startActivity(intent);
    }
}
