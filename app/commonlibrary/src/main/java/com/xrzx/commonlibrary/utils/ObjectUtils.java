package com.xrzx.commonlibrary.utils;

import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/6 14:29
 */
public class ObjectUtils {
    /**
     * 获取类的基本信息
     *
     * @param mClass 类
     * @return 信息
     */
    public static HashMap<String, FieldInfo> getFieldInfo(Class mClass, boolean sqLite) {
        HashMap<String, FieldInfo> fieldInfoHashMap = new HashMap<>(16);
        final ArrayList<Method> setMethodList = SQLiteUtils.getMethodBySet(mClass);
        final ArrayList<Method> getMethodList = SQLiteUtils.getMethodByGet(mClass);
        final ArrayList<Field> fieldList = sqLite ? SQLiteUtils.getSortFieldByOrder(mClass) : getFieldList(mClass);
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

    public static ArrayList<Field> getFieldList(Class mClass) {
        Field[] fs = mClass.getDeclaredFields();
        return new ArrayList<>(Arrays.asList(fs));
    }

    /**
     * 复制对象
     *
     * @param mClass 类
     * @param src    源
     * @param dest   目标
     */
    public static void mClone(Class<?> mClass, Object src, Object dest) {
        final HashMap<String, FieldInfo> fieldInfoHashMap = getFieldInfo(mClass, true);
        for (String key : fieldInfoHashMap.keySet()) {
            final FieldInfo fieldInfo = fieldInfoHashMap.get(key);
            if (null == fieldInfo) {
                continue;
            }
            final Method getMethod = fieldInfo.getGetMethod();
            final Method setMethod = fieldInfo.getSetMethod();
            try {
                final Object invoke = getMethod.invoke(src);
                if (null == invoke) {
                    continue;
                }
                setMethod.invoke(dest, invoke);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
