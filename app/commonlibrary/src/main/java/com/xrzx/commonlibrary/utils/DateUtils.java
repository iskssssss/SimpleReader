package com.xrzx.commonlibrary.utils;
import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xrzx.commonlibrary.enums.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description 时间工具类
 * @Author ks
 * @Date 2020/4/12 23:48
 */
public class DateUtils {

    public enum TimestampType {
        /**
         * 秒
         */
        SECOND,
        /**
         * 毫秒
         */
        MILLISECOND;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间（毫秒）
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();

    }

    /**
     * 将 String 类型的时间格式转为 DateTimeFormatter
     *
     * @param format 格式
     * @return DateTimeFormatter
     */
    private static DateTimeFormatter toDataTimeFormat(String format) {
        return DateTimeFormatter.ofPattern(format);
    }

    /**
     * 获取时间戳
     * @param timestampType 秒/毫秒
     * @return 时间戳
     */
    public static Long getTimestamp(TimestampType timestampType) {
        if (timestampType == TimestampType.SECOND) {
            return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        }
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 将时间戳转换为LocalDateTime
     * @param timestamp 时间戳
     * @param timestampType 时间戳类型
     * @return LocalDateTime
     */
    public static LocalDateTime timestamp2LocalDateTime(Long timestamp, TimestampType timestampType) {
        if (timestampType == TimestampType.SECOND) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    /**
     * 获取当前时间
     *
     * @param format 指定格式
     * @return 时间
     */
    public static String getDateTime(String format) {
        return now().format(toDataTimeFormat(format));
    }

    /**
     * 获取当前时间(yyyy-MM-dd HH:mm:ss)
     *
     * @return 当前时间
     */
    public static String getDateTime() {
        return getDateTime(DateTimeFormat.DATE_TIME_FORMAT.getFormat());
    }

    /**
     * 获取当前日期(yyyy-MM-dd)
     *
     * @return 当前时间
     */
    public static String getDate() {
        return getDateTime(DateTimeFormat.DATE_FORMAT.getFormat());
    }

    /**
     * 获取当前时间(HH:mm:ss)
     *
     * @return 当前时间
     */
    public static String getTime() {
        return getDateTime(DateTimeFormat.TIME_FORMAT.getFormat());
    }

    /**
     * 将字符串时间转换为 LocalDateTime (yyyy-MM-dd HH:mm:ss)
     *
     * @return 当前时间
     */
    public static LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormat.DATE_TIME_FORMAT.getDateTimeFormat());
    }

    /**
     * 将字符串时间转换为 LocalDate (yyyy-MM-dd)
     *
     * @return 当前时间
     */
    public static LocalDate toLocalDate(String date) {
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * 将字符串时间转换为 LocalTime (HH:mm:ss)
     *
     * @return 当前时间
     */
    public static LocalTime toLocalTime(String time) {
        return toLocalDateTime(time).toLocalTime();
    }

    /**
     * 当前时间减去一个特定的时间
     *
     * @param amountToSubtract 时间
     * @param unit             类型
     * @return LocalDateTime
     */
    public static LocalDateTime minus(long amountToSubtract, TemporalUnit unit) {
        return now().minus(amountToSubtract, unit);
    }

    /**
     * 将 LocalDateTime 转为字符串
     *
     * @param date 日期
     * @return String
     */
    public static String toString(LocalDateTime date) {
        return DateTimeFormat.DATE_TIME_FORMAT.getDateTimeFormat().format(date);
    }

    /**
     * 获取两个时间差
     *
     * @param startTime 开始时间
     * @param endTime   现在时间
     * @return 差
     */
    public static Duration duration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    /**
     * 获取两个时间差（毫秒）
     *
     * @param startTime 开始时间
     * @param endTime   现在时间
     * @return 差
     */
    public static long durationByMillis(LocalDateTime startTime, LocalDateTime endTime) {
        return duration(startTime, endTime).toMillis();
    }

    /**
     * 将字符串转为Date
     *
     * @return Date
     */
    public static Date toDate(String dateTime) {
        return Date.from(toLocalDateTime(dateTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将Date转为字符串
     *
     * @param date 日期
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
    public static String toString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DateTimeFormat.DATE_TIME_FORMAT.getFormat());
        return format.format(date);
    }

    public static String getDate(String dateTime) {
        return dateTime.split(" ")[0];
    }
}
