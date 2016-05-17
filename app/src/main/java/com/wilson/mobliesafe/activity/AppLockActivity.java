package com.wilson.mobliesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.wilson.mobliesafe.R;

public class AppLockActivity extends FragmentActivity implements View.OnClickListener {
    private TextView tv_unlock;
    private TextView tv_lock;
    private FragmentManager fragmentManager;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_lock);
        initUI();
    }

    private void initUI() {
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);
        tv_unlock.setOnClickListener(this);
        tv_lock.setOnClickListener(this);
        //获取到fragment的管理者
        fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();
        unLockFragment = new UnLockFragment();
        lockFragment = new LockFragment();
        /**
         * 替换界面
         * 1 需要替换的界面的id
         * 2具体指某一个fragment的对象
         */
        mTransaction.replace(R.id.fl_content, unLockFragment).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_lock:
                //没有加锁
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_lock.setBackgroundResource(R.drawable.tab_right_default);

                ft.replace(R.id.fl_content, lockFragment);
                break;

            case R.id.tv_unlock:
                //没有加锁
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);

                ft.replace(R.id.fl_content, unLockFragment);
                break;
        }
        ft.commit();
    }
}
