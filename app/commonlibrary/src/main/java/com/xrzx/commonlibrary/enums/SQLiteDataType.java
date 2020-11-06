package com.xrzx.commonlibrary.enums;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/4 18:49
 */
public enum SQLiteDataType {
    /**
     * 字符型
     */
    TEXT("TEXT"),
    /**
     * 数值型
     */
    INTEGER("INTEGER"),
    /**
     * 数值型
     */
    LONG("INTEGER");

    private final String type;

    SQLiteDataType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
