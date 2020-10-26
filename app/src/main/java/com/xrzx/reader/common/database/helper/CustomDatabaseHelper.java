package com.xrzx.reader.common.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @Description 数据库操作
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class CustomDatabaseHelper extends SQLiteOpenHelper {

    private static volatile CustomDatabaseHelper customDatabaseHelper;


    private CustomDatabaseHelper(@Nullable Context context){
        super(context, "Blcs", null, 1);
    }

    public static CustomDatabaseHelper getCustomDatabaseHelper(@Nullable Context context) {
        if (customDatabaseHelper == null) {
            synchronized (CustomDatabaseHelper.class) {
                if (customDatabaseHelper == null) {
                    customDatabaseHelper = new CustomDatabaseHelper(context);
                }
            }
        }
        return customDatabaseHelper;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("onUpgrade.empty");
    }
}
