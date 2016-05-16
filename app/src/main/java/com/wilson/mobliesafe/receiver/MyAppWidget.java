package com.wilson.mobliesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.wilson.mobliesafe.service.KillProcesWidgetService;

public class MyAppWidget extends AppWidgetProvider {

    /**
     * 第一次创建的时候才会调用当前的生命周期的方法
     * <p>
     * 当前的广播的生命周期只有10秒钟。
     * 不能做耗时的操作
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, KillProcesWidgetService.class);
        context.startService(intent);
    }

    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, KillProcesWidgetService.class);
        context.stopService(intent);
    }
}
