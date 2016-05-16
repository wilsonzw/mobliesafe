package com.wilson.mobliesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wilson.mobliesafe.R;
import com.wilson.mobliesafe.utils.SmsUtils;
import com.wilson.mobliesafe.utils.UIUtils;

/**
 * 高级工具
 *
 * @author wilson
 */
public class AToolsActivity extends Activity {
    private ProgressDialog pd;
    @ViewInject(R.id.progressBar1)
    private ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        ViewUtils.inject(this);
    }

    /**
     * 归属地查询
     *
     * @param view click
     */
    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    /**
     * 备份短信
     *
     * @param view
     */
    public void backUpsms(View view) {
        //初始化一个进度条的对话框
        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("提示");
        pd.setMessage("稍安勿躁。正在备份。你等着吧。。");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        new Thread() {
            public void run() {
                boolean result = SmsUtils.backUp(AToolsActivity.this, new SmsUtils.BackUpCallBackSms() {

                    @Override
                    public void onBackUpSms(int process) {
                        pd.setProgress(process);
                        progressBar1.setProgress(process);

                    }

                    @Override
                    public void befor(int count) {
                        pd.setMax(count);
                        progressBar1.setMax(count);
                    }
                });
                if (result) {
                    //安全弹吐司的方法
                    UIUtils.showToast(AToolsActivity.this, "备份成功");
                } else {
                    UIUtils.showToast(AToolsActivity.this, "备份失败");
                }
                pd.dismiss();
            }
        }.start();


    }

}
