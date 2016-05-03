package com.wilson.mobliesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 黑马程序员教育集团版权所有(c) 2015
 * <p/>
 * 作     者  :  马伟奇
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2015/2/28  9:50
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class AppInfo {
    /**
     * 图片的icon
     */
    private Drawable icon;

    /**
     * 程序的名字
     */
    private String apkName;

    /**
     * 程序大小
     */
    private long apkSize;

    /**
     * 表示到底是用户app还是系统app
     * 如果表示为true 就是用户app
     * 如果是false表示系统app
     */
    private boolean userApp;
    /**
     * 放置的位置
     */
    private boolean isRom;

    /**
     * 包名
     */
    private String apkPackageName;


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean isRom) {
        this.isRom = isRom;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", userApp=" + userApp +
                ", isRom=" + isRom +
                ", apkPackageName='" + apkPackageName + '\'' +
                '}';
    }
}












