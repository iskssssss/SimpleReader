package com.xrzx.commonlibrary.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xrzx.commonlibrary.database.dao.base.BaseDao;
import com.xrzx.commonlibrary.entity.FieldInfo;
import com.xrzx.commonlibrary.entity.ReadPageSetting;
import com.xrzx.commonlibrary.entity.ReadPageSettingLog;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.ObjectUtils;
import com.xrzx.commonlibrary.utils.SQLiteUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/7 23:46
 */
public class ReadPageSettingDao extends BaseDao {
    private static final String TABLE_NAME = SQLiteUtils.entityName2SqlName(ReadPageSetting.class.getSimpleName());
    private static final String[] COLUMNS = SQLiteUtils.getDeclaredFieldsPressSqlType(ReadPageSetting.class);

    /**
     * 创建数据表
     *
     * @param db
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateSql(TABLE_NAME, ReadPageSetting.class));

        ReadPageSettingLog readPageSettingLog = new ReadPageSettingLog();
        readPageSettingLog.setFontSize(0);
        readPageSettingLog.setFontSize(22);
        readPageSettingLog.setRowSpacing(16);
        readPageSettingLog.setPageSpacing(24);
        readPageSettingLog.setLuminance(50);
        readPageSettingLog.setLuminanceSystem(1);
        readPageSettingLog.setAtNight(0);
        readPageSettingLog.setHorizontalScreen(0);
        readPageSettingLog.setReadPageStyle(ReadPageStyle.READ_YD_K61);
        ReadPageSettingDao.insertAll(readPageSettingLog, db);
    }

    public static void insertAll(ReadPageSettingLog readPageSettingLog) {
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            insertAll(readPageSettingLog, db);
        }
    }

    public static void insertAll(ReadPageSettingLog readPageSettingLog, SQLiteDatabase db) {
        final HashMap<String, FieldInfo> fieldInfoMap = ObjectUtils.getFieldInfo(ReadPageSettingLog.class, false);
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        for (String key : fieldInfoMap.keySet()) {
            final FieldInfo fieldInfo = fieldInfoMap.get(key);
            final Method getMethod = fieldInfo.getGetMethod();
            try {
                final Object invoke = getMethod.invoke(readPageSettingLog);
                if (null == invoke) {
                    continue;
                }
                contentValues.put("rps_key", key);
                contentValues.put("rps_value", String.valueOf(invoke));
                db.insert(TABLE_NAME, null, contentValues);
                contentValues.clear();
            } catch (Exception e) {
                if (null != contentValues) {
                    contentValues.clear();
                }
                e.printStackTrace();
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void read(ReadPageSettingLog readPageSettingLog) {
        final HashMap<String, FieldInfo> fieldInfoMap = ObjectUtils.getFieldInfo(ReadPageSettingLog.class, false);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            try (Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null)) {
                while (cursor.moveToNext()) {
                    final FieldInfo fieldInfo = fieldInfoMap.get(cursor.getString(cursor.getColumnIndex("rps_key")));
                    final String rpsValue = cursor.getString(cursor.getColumnIndex("rps_value"));
                    if (null == fieldInfo) {
                        continue;
                    }
                    final Field field = fieldInfo.getField();
                    final String typeSimpleName = field.getType().getSimpleName();
                    final Method setMethod = fieldInfo.getSetMethod();
                    try {
                        switch (typeSimpleName) {
                            case "int":
                                setMethod.invoke(readPageSettingLog, Integer.parseInt(rpsValue));
                                break;
                            case "ReadPageStyle":
                                setMethod.invoke(readPageSettingLog, ReadPageStyle.valueOf(rpsValue));
                                break;
                            default:
                                continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("");
                }

            }
        }
    }

    public static void update(String rpsKey, String rpsValue) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("rps_value", rpsValue);
        try (SQLiteDatabase db = CUSTOM_DATABASE_HELPER.getWritableDatabase()) {
            db.update(TABLE_NAME, contentValues, "rps_key = ?", new String[]{rpsKey});
        }
    }
}
