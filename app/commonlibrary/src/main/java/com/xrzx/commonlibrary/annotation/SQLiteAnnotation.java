package com.xrzx.commonlibrary.annotation;

import com.xrzx.commonlibrary.enums.SQLiteDataType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/31 23:07
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SQLiteAnnotation {

    /**
     * 数据类型
     */
    SQLiteDataType dataType();

    /**
     * 顺序
     */
    int order();

    /**
     * 主键
     */
    boolean key() default false;

    /**
     * 自增长
     */
    boolean autoincrement() default false;

    /**
     * 是否为空
     */
    boolean notNull() default false;
}
