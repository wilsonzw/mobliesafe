package com.wilson.mobliesafe.activity;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.wilson.mobliesafe.R;

import java.lang.reflect.Method;
import java.util.List;

public class CleanCacheActivity extends Activity {
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        initUI();
    }

    private void initUI() {
        packageManager = getPackageManager();
        /**
         * 接收2个参数
         * 第一个参数接收一个包名
         * 第二个参数接收aidl的对象
         */
//		  * @hide
//		     */
//		public abstract void getPackageSizeInfo(String packageName,IPackageStatsObserver observer);
//		packageManager.getPackageSizeInfo();
        //安装到手机上面所有的应用程序
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            getCacheSize(packageInfo);
        }
    }

    private void getCacheSize(PackageInfo packageInfo) {
        try {
            Class<?> clazz = getClassLoader().loadClass("packageManager");
            //通过反射获取到当前的方法
            Method method = clazz.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageInfo.applicationInfo.packageName, new MyIPackageStatsObserver(packageInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MyIPackageStatsObserver extends IPackageStatsObserver.Stub {
        private PackageInfo packageInfo;

        public MyIPackageStatsObserver(PackageInfo packageInfo) {
            this.packageInfo = packageInfo;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cacheSize = pStats.cacheSize;
            if (cacheSize > 0) {

            }
        }
    }

    private void cleanAll(View view) {
        Method[] methods = PackageManager.class.getMethods();
        for (Method method : methods) {
            if ("freeStorageAndNotify".equals(method.getName())) {
                try {
                    method.invoke(packageManager, Integer.MAX_VALUE, new MyIPackageDataObserver());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyIPackageDataObserver extends IPackageDataObserver.Stub {

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }
}
