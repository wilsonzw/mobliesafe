package com.wilson.mobliesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class AntivirusDao {

    /**
     * 检查当前的md5值是否在病毒数据库
     *
     * @param md5 特征码
     * @return desc 描述信息
     */
    public static String checkFileVirus(String md5) {
        String desc = null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.wilson.mobliesafe/files/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
        //查询当前传过来的md5是否在病毒数据库里面
        Cursor cursor = db.rawQuery("select desc from datable where md5 = ?", new String[]{md5});
        //判断当前的游标是否可以移动
        if (cursor.moveToNext()) {
            desc = cursor.getString(0);
        }
        cursor.close();
        return desc;
    }

    /**
     * 添加病毒数据库
     *
     * @param md5  特征码
     * @param desc 描述信息
     */
    public static void addVirus(String md5, String desc) {
        if (!TextUtils.isEmpty(checkFileVirus(md5))) {
            return;
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.wilson.mobliesafe/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5", md5);
        values.put("type", 6);
        values.put("name", "Android.Troj.AirAD.a");
        values.put("desc", desc);
        db.insert("datable", null, values);
        db.close();
    }

}

