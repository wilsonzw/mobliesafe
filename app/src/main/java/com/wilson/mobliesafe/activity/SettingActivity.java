package com.wilson.mobliesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wilson.mobliesafe.R;
import com.wilson.mobliesafe.service.AddressService;
import com.wilson.mobliesafe.service.CallSafeService;
import com.wilson.mobliesafe.utils.ServiceStatusUtils;
import com.wilson.mobliesafe.view.SettingClickView;
import com.wilson.mobliesafe.view.SettingItemView;

/**
 * 设置中心
 *
 * @author wilson
 */
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;// 设置升级
    private SettingItemView sivAddress;// 设置升级
    private SettingClickView scvAddressStyle;// 修改风格
    private SettingClickView scvAddressLocation;// 修改归属地位置
    private SharedPreferences mPref;
    private SettingItemView siv_callsafe;// 黑名单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
        initBlackView();
    }

    /**
     * 初始化黑名单
     */
    private void initBlackView() {
        siv_callsafe = (SettingItemView) findViewById(R.id.siv_callsafe);

        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.wilson.mobliesafe.service.CallSafeService");
        siv_callsafe.setChecked(serviceRunning);

        siv_callsafe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (siv_callsafe.isChecked()) {
                    siv_callsafe.setChecked(false);
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));// 停止归属地服务
                } else {
                    siv_callsafe.setChecked(true);
                    startService(new Intent(SettingActivity.this, CallSafeService.class));// 开启归属地服务
                }
            }
        });
    }

    /**
     * 初始化自动更新开关
     */
    private void initUpdateView() {
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        // sivUpdate.setTitle("自动更新设置");

        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        sivUpdate.setChecked(autoUpdate);

        sivUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                boolean curIsChecked = !sivUpdate.isChecked();
                sivUpdate.setChecked(curIsChecked);
                mPref.edit().putBoolean("auto_update", curIsChecked).apply();
            }
        });
    }

    /**
     * 初始化归属地开关
     */
    private void initAddressView() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);

        // 根据归属地服务是否运行来更新checkbox
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.wilson.mobliesafe.service.AddressService");
        sivAddress.setChecked(serviceRunning);

        sivAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));// 停止归属地服务
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));// 开启归属地服务
                }
            }
        });
    }

    final String[] items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

    /**
     * 修改提示框显示风格
     */
    private void initAddressStyle() {
        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);

        scvAddressStyle.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
    }

    /**
     * 弹出选择风格的单选框
     */
    protected void showSingleChooseDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style

        builder.setSingleChoiceItems(items, style,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPref.edit().putInt("address_style", which).apply();// 保存选择的风格
                        dialog.dismiss();// 让dialog消失
                        scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
            }
        });
    }
}
