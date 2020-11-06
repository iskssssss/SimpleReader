package com.xrzx.commonlibrary.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/2 12:20
 */
public class FieldInfo {
    Field field;
    Method setMethod;
    Method getMethod;

    public FieldInfo(Field field, Method setMethod, Method getMethod) {
        this.field = field;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "field=" + field +
                ", getMethod=" + getMethod +
                ", setMethod=" + setMethod +
                '}';
    }
}
