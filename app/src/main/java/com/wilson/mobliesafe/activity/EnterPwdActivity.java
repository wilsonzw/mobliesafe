package com.wilson.mobliesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wilson.mobliesafe.R;
import com.wilson.mobliesafe.utils.UIUtils;

public class EnterPwdActivity extends Activity implements View.OnClickListener {
    private EditText et_pwd;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        initUI();
    }

    private void initUI() {
        Intent intent = getIntent();
        if (intent != null) {
            packageName = intent.getStringExtra("packageName");
        }
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        // 隐藏当前的键盘
        et_pwd.setInputType(InputType.TYPE_NULL);
        findViewById(R.id.bt_0).setOnClickListener(this);
        findViewById(R.id.bt_1).setOnClickListener(this);
        findViewById(R.id.bt_2).setOnClickListener(this);
        findViewById(R.id.bt_3).setOnClickListener(this);
        findViewById(R.id.bt_4).setOnClickListener(this);
        findViewById(R.id.bt_5).setOnClickListener(this);
        findViewById(R.id.bt_6).setOnClickListener(this);
        findViewById(R.id.bt_7).setOnClickListener(this);
        findViewById(R.id.bt_8).setOnClickListener(this);
        findViewById(R.id.bt_9).setOnClickListener(this);
        findViewById(R.id.bt_ok).setOnClickListener(this);
        findViewById(R.id.bt_clean_all).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str;
        switch (v.getId()) {
            case R.id.bt_ok:
                String result = et_pwd.getText().toString();
                if ("123".equals(result)) {
                    Intent intent = new Intent();
                    // 发送广播。停止保护
                    intent.setAction("com.wilson.mobliesafe.stopprotect");
                    // 跟狗说。现在停止保护短信
                    intent.putExtra("packageName", packageName);
                    sendBroadcast(intent);
                    finish();
                } else {
                    UIUtils.showToast(EnterPwdActivity.this, "密码错误");
                }
                break;
            case R.id.bt_clean_all: // 清空
                et_pwd.setText("");
                break;
            case R.id.bt_delete:  // 删除
                str = et_pwd.getText().toString();
                if (str.length() == 0) {
                    return;
                }
                et_pwd.setText(str.substring(0, str.length() - 1));
                break;
            case R.id.bt_0:
            case R.id.bt_1:
            case R.id.bt_2:
            case R.id.bt_3:
            case R.id.bt_4:
            case R.id.bt_5:
            case R.id.bt_6:
            case R.id.bt_7:
            case R.id.bt_8:
            case R.id.bt_9:
                str = et_pwd.getText().toString() + ((Button) v).getText().toString();
                et_pwd.setText(str);
                break;
        }
    }

    // 监听当前页面的后退健
    // <intent-filter>
    // <action android:name="android.intent.action.MAIN" />
    // <category android:name="android.intent.category.HOME" />
    // <category android:name="android.intent.category.DEFAULT" />
    // <category android:name="android.intent.category.MONKEY"/>
    // </intent-filter>
    @Override
    public void onBackPressed() {
        // 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

}
