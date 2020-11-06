package com.xrzx.commonlibrary.utils;

/**
 * @Description 字符串处理工具类
 * @Author ks
 * @Date 2020/3/28 19:15
 */
public class StringUtils {

    /**
     * 判断是否相等
     *
     * @param object1 字符串1
     * @param object2 字符串2
     * @return 结果
     */
    public static boolean equals(String object1, String object2) {
        return object1.equals(object2);
    }

    /**
     * 判断字符串是否为空.
     *
     * @param object 对象
     * @return 是否为null或''
     */
    public static boolean isEmpty(String object) {
        return isNotNull(object) && "".equals(object);
    }

    /**
     * 判断对象是否为''.
     *
     * @param object 对象
     * @return 是否为null或''
     */
    public static boolean isNotEmpty(String object) {
        return !isEmpty(object);
    }

    /**
     * 判断对象是否为null
     *
     * @param object 对象
     * @return 是否为null
     */
    public static boolean isNull(Object object) {
        return null == object;
    }

    /**
     * 判断对象是否不为null
     *
     * @param object 对象
     * @return 是否为null
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * char -> String
     *
     * @param c char数据
     * @return String
     */
    public static String toString(char c) {
        char[] data = {c};
        return toString(data);
    }

    /**
     * char[] -> String
     *
     * @param chars char[]数据
     * @return String
     */
    public static String toString(char[] chars) {
        return String.valueOf(chars);
    }

    /**
     * String -> char[]
     *
     * @param str String数据
     * @return char[]
     */
    public static char[] toArray(String str) {
        return str.toCharArray();
    }

    /**
     * Long -> String
     *
     * @param val String数据
     * @return char[]
     */
    public static String toString(Long val) {
        return String.valueOf(val);
    }

    /**
     * String -> Long
     *
     * @param val String数据
     * @return char[]
     */
    public static Long toLong(String val) throws NumberFormatException {
        return Long.valueOf(val);
    }

    /**
     * 删除头部字符
     *
     * @param val 字符串
     * @return 结果
     */
    public static String delHeadChar(String val) {
        return val.substring(1);
    }

    /**
     * 删除尾部字符
     *
     * @param val 字符串
     * @return 结果
     */
    public static String delTailChar(String val) {
        return val.substring(0, val.length() - 1);
    }

    /**
     * 删除头部和尾部字符
     *
     * @param val 字符串
     * @return 结果
     */
    public static String delHeadAndTailChar(String val) {
        return delHeadChar(delTailChar(val));
    }
}
