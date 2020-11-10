package com.xrzx.commonlibrary.utils;


import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/1 18:53
 */
public class SQLiteUtils {

    /**
     * 获取类的方法名(数据库格式)
     *
     * @param mClass 类
     * @return
     */
    public static String[] getDeclaredFieldsPressSqlType(Class mClass) {
        List<Field> fields = getSortFieldByOrder(mClass);
        String[] declaredFields = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getName();
            declaredFields[i] = entityName2SqlName(fieldName);
        }
        return declaredFields;
    }

    /**
     *
     * 获取类的方法(get)
     * @param mClass 类
     * @return  方法列表
     */
    public static ArrayList<Method> getMethodByGet(Class mClass ) {
        return getMethod(mClass, "get");
    }

    /**
     *
     * 获取类的方法(set)
     * @param mClass 类
     * @return  方法列表
     */
    public static ArrayList<Method> getMethodBySet(Class mClass ) {
        return getMethod(mClass, "set");
    }

    /**
     * 获取类的方法
     * @param mClass 类
     * @param type 获取类型
     * @return  方法列表
     */
    public static ArrayList<Method> getMethod(Class mClass , String type) {
        final Method[] methods = mClass.getMethods();
        ArrayList<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (!method.getName().toLowerCase().contains(type)) {
                continue;
            }
            methodList.add(method);
        }
        return methodList;
    }


    /**
     * 获取类的方法名(字典)
     *
     * @param mClass 类
     * @return
     */
    public static LinkedHashMap<String, String> getDeclaredFieldDict(Class mClass) {
        List<Field> fields = getSortFieldByOrder(mClass);
        LinkedHashMap<String, String> fieldMaps = new LinkedHashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getName();
            fieldMaps.put(fieldName, entityName2SqlName(fieldName));
        }
        return fieldMaps;
    }

    /**
     * 排序字段  通过order进行字段的排序
     *
     * @param mClass  类
     * @return 排序后的字段列表
     */
    public static ArrayList<Field> getSortFieldByOrder(Class mClass) {
        Field[] fs = mClass.getDeclaredFields();
        ArrayList<Field> fields = new ArrayList<>();
        for (Field f : fs) {
            if (!f.isAnnotationPresent(SQLiteAnnotation.class) ) {
                continue;
            }
            fields.add(f);
        }
            fields.sort(Comparator.comparingInt(m -> m.getAnnotation(SQLiteAnnotation.class).order()));
        return fields;
    }

    /**
     * 将字段名中的大写字母转换为 _X 格式
     * <p>
     * 如 aBc  = a_bc
     *
     * @param name 字段名
     * @return 转换后的字段名
     */
    public static String entityName2SqlName(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (c > 64 && c < 91) {
                if (i > 0){
                    stringBuilder.append("_");
                }
                stringBuilder.append((char) (c + 32));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * 将字段名中 _X格式的字符 转换为相应的大写字母
     * <p>
     * 如 a_bc = aBc
     *
     * @param name 字段名
     * @return 转换后的字段名
     */
    public static String sqlName2EntityName(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
             char c = name.charAt(i);
            if (c == '_'){
                c = name.charAt(++i);
                stringBuilder.append((char) (c - 32));
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
