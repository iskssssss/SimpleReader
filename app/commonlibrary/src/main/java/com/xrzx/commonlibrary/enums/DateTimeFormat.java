package com.xrzx.commonlibrary.enums;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;

/**
 * @Description 时间格式
 * @Author eternity
 * @Date 2020/4/14 20:07
 */
public enum DateTimeFormat {
    /**
     * 日期+时间格式
     */
    DATE_TIME_FORMAT("yyyy-MM-dd HH:mm:ss"),
    /**
     * 日期格式
     */
    DATE_FORMAT("yyyy-MM-dd"),
    /**
     * 时间格式
     */
    TIME_FORMAT("HH:mm:ss");

    private final String format;
    DateTimeFormat(String format){
        this.format = format;
    }

    public DateTimeFormatter getDateTimeFormat() {
        return DateTimeFormatter.ofPattern(format);
    }

    public String getFormat() {
        return format;
    }
}
