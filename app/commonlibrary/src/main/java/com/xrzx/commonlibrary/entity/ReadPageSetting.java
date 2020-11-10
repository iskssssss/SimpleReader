package com.xrzx.commonlibrary.entity;

import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.enums.SQLiteDataType;

import java.io.Serializable;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/7 17:22
 */
public class ReadPageSetting implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 设置序号
     */
    @SQLiteAnnotation(order = 1, dataType = SQLiteDataType.LONG, key = true, autoincrement = true, notNull = true)
    private Long rpsId;

    /**
     * 设置键
     */
    @SQLiteAnnotation(order = 2, dataType = SQLiteDataType.TEXT, notNull = true)
    private String rpsKey;

    /**
     * 设置值
     */
    @SQLiteAnnotation(order = 3, dataType = SQLiteDataType.TEXT, notNull = true)
    private String rpsValue;

    public Long getRpsId() {
        return rpsId;
    }

    public void setRpsId(Long rpsId) {
        this.rpsId = rpsId;
    }

    public String getRpsKey() {
        return rpsKey;
    }

    public void setRpsKey(String rpsKey) {
        this.rpsKey = rpsKey;
    }

    public String getRpsValue() {
        return rpsValue;
    }

    public void setRpsValue(String rpsValue) {
        this.rpsValue = rpsValue;
    }

    @Override
    public String toString() {
        return "ReadPageSetting{" +
                "rpsId=" + rpsId +
                ", rpsKey='" + rpsKey + '\'' +
                ", rpsValue='" + rpsValue + '\'' +
                '}';
    }
}
