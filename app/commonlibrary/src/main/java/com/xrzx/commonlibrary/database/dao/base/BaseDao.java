package com.xrzx.commonlibrary.database.dao.base;

import android.content.ContentValues;
import android.database.Cursor;

import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.entity.FieldInfo;
import com.xrzx.commonlibrary.enums.SQLiteDataType;
import com.xrzx.commonlibrary.utils.SQLiteUtils;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.database.helper.CustomDatabaseHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/26 19:24
 */
public class BaseDao {
    protected static final CustomDatabaseHelper CUSTOM_DATABASE_HELPER = CustomDatabaseHelper.getCustomDatabaseHelper();

    /**
     * 通过类生成创建表的SQL语句
     *
     * @param mClass 类
     * @return SQL语句
     */
    protected static String getCreateSql(Class mClass) {
        List<Field> fields = SQLiteUtils.getSortFieldByOrder(mClass);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(mClass.getSimpleName().toLowerCase());
        sb.append("\n(");
        for (Field field : fields) {
            final SQLiteAnnotation annotation = field.getAnnotation(SQLiteAnnotation.class);
            String fieldName = field.getName();
            sb.append(SQLiteUtils.entityName2SqlName(fieldName));
            sb.append(" ");
            sb.append(annotation.dataType().getType());
            if (annotation.key()) {
                sb.append(" ");
                sb.append("PRIMARY KEY");
            }
            if (annotation.autoincrement()) {
                sb.append(" ");
                sb.append("AUTOINCREMENT");
            }
            if (annotation.notNull()) {
                sb.append(" ");
                sb.append("NOT NULL");
            }
            sb.append(",");
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(");");
        return sb.toString();
    }

    /**
     * 获取类的基本信息
     *
     * @param mClass 类
     * @return 信息
     */
    protected static HashMap<String, FieldInfo> getFieldInfo(Class mClass) {
        HashMap<String, FieldInfo> fieldInfoHashMap = new HashMap<>(16);
        final ArrayList<Method> setMethodList = SQLiteUtils.getMethodBySet(mClass);
        final ArrayList<Method> getMethodList = SQLiteUtils.getMethodByGet(mClass);
        final ArrayList<Field> fieldList = SQLiteUtils.getSortFieldByOrder(mClass);
        for (Field field : fieldList) {
            final String entityName = field.getName();
            final String sqlName = SQLiteUtils.entityName2SqlName(entityName);
            Method setMethod = null;
            final Iterator<Method> setMethodListIterator = setMethodList.iterator();
            while (setMethodListIterator.hasNext()) {
                final Method next = setMethodListIterator.next();
                if (next.getName().toLowerCase().contains("set" + entityName.toLowerCase())) {
                    setMethod = next;
                    setMethodListIterator.remove();
                    break;
                }
            }
            Method getMethod = null;
            final Iterator<Method> getMethodListIterator = getMethodList.iterator();
            while (getMethodListIterator.hasNext()) {
                final Method next = getMethodListIterator.next();
                if (next.getName().toLowerCase().contains("get" + entityName.toLowerCase())) {
                    getMethod = next;
                    getMethodListIterator.remove();
                    break;
                }
            }
            fieldInfoHashMap.put(sqlName, new FieldInfo(field, setMethod, getMethod));
        }
        setMethodList.clear();
        getMethodList.clear();
        fieldList.clear();
        return fieldInfoHashMap;
    }

    /**
     * 将数据写入实体中
     *
     * @param objectList 实体列表
     * @param mClass     类
     * @param cursor     数据库游标
     * @return 结果
     */
    protected static boolean writeEntity(List<Object> objectList, Class mClass, Cursor cursor) {
        final HashMap<String, FieldInfo> fieldInfoHashMap = getFieldInfo(mClass);
        while (cursor.moveToNext()) {
            Object chapter;
            try {
                chapter = mClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return false;
            }
            for (String columnName : cursor.getColumnNames()) {
                final FieldInfo fieldInfo = fieldInfoHashMap.get(columnName);
                final SQLiteAnnotation annotation = fieldInfo.getField().getAnnotation(SQLiteAnnotation.class);
                final SQLiteDataType sqLiteDataType = annotation.dataType();
                try {
                    switch (sqLiteDataType) {
                        case LONG:
                            fieldInfo.getSetMethod().invoke(chapter, cursor.getLong(cursor.getColumnIndex(columnName)));
                            break;
                        case INTEGER:
                            fieldInfo.getSetMethod().invoke(chapter, cursor.getInt(cursor.getColumnIndex(columnName)));
                            break;
                        case TEXT:
                        default:
                            fieldInfo.getSetMethod().invoke(chapter, cursor.getString(cursor.getColumnIndex(columnName)));
                            break;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            objectList.add(chapter);
        }
        fieldInfoHashMap.clear();
        return true;
    }

    /**
     * 将实体转换为 ContentValues
     *
     * @param mClass 类
     * @param entity 实体
     * @return ContentValues
     */
    public static ContentValues getContentValues(Class mClass, Object entity) {
        ContentValues contentValues = new ContentValues();
        final HashMap<String, FieldInfo> fieldInfoHashMap = getFieldInfo(mClass);
        for (String key : fieldInfoHashMap.keySet()) {
            final FieldInfo fieldInfo = fieldInfoHashMap.get(key);
            final Method getMethod = fieldInfo.getGetMethod();
            try {
                final Object invoke = getMethod.invoke(entity);
                if (null == invoke) {
                    continue;
                }
                final SQLiteAnnotation annotation = fieldInfo.getField().getAnnotation(SQLiteAnnotation.class);
                final SQLiteDataType sqLiteDataType = annotation.dataType();
                switch (sqLiteDataType) {
                    case LONG:
                        contentValues.put(key, (long) invoke);
                        break;
                    case INTEGER:
                        contentValues.put(key, (int) invoke);
                        break;
                    case TEXT:
                    default:
                        contentValues.put(key, (String) invoke);
                        break;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }
}
