package com.xrzx.commonlibrary.entity;

import androidx.annotation.NonNull;

import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.enums.SQLiteDataType;

import java.io.Serializable;

/**
 * @Description 章节信息实体类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class Chapter implements Serializable, Cloneable {
    private static final long serialVersionUID = 42L;

    /**
     * 章节id
     */
    @SQLiteAnnotation(order = 1, dataType = SQLiteDataType.LONG, key = true, autoincrement = true, notNull = true)
    private Long cId;
    /**
     * 书籍唯一标识
     */
    @SQLiteAnnotation(order = 2, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bUniquelyIdentifies;
    /**
     * 章节序号
     */
    @SQLiteAnnotation(order = 3, dataType = SQLiteDataType.INTEGER, notNull = true)
    private Integer cNumber;
    /**
     * 章节标题
     */
    @SQLiteAnnotation(order = 4, dataType = SQLiteDataType.TEXT, notNull = true)
    private String cTitle;
    /**
     * 章节链接
     */
    @SQLiteAnnotation(order = 5, dataType = SQLiteDataType.TEXT, notNull = true)
    private String cUrl;
    /**
     * 章节正文
     */
    @SQLiteAnnotation(order = 6, dataType = SQLiteDataType.TEXT)
    private String cContent;

    public Chapter() {
    }
    public Chapter(String cTitle, String cUrl) {
        this.cTitle = cTitle;
        this.cUrl = cUrl;
    }
    public Chapter(Integer cNumber, String cTitle, String cUrl) {
        this.cNumber = cNumber;
        this.cTitle = cTitle;
        this.cUrl = cUrl;
    }
    public Chapter(String bUniquelyIdentifies, Integer cNumber, String cTitle, String cUrl) {
        this.bUniquelyIdentifies = bUniquelyIdentifies;
        this.cNumber = cNumber;
        this.cTitle = cTitle;
        this.cUrl = cUrl;
    }

    //---------------------------------------------------------------------------------------------------------------
    // start 基础get/set方法

    public Long getcId() {
        return cId;
    }
    public void setcId(Long cId) {
        this.cId = cId;
    }
    public String getbUniquelyIdentifies() {
        return bUniquelyIdentifies;
    }
    public void setbUniquelyIdentifies(String bUniquelyIdentifies) {
        this.bUniquelyIdentifies = bUniquelyIdentifies;
    }
    public Integer getcNumber() {
        return cNumber;
    }
    public void setcNumber(Integer cNumber) {
        this.cNumber = cNumber;
    }
    public String getcTitle() {
        return cTitle;
    }
    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }
    public String getcUrl() {
        return cUrl;
    }
    public void setcUrl(String cUrl) {
        this.cUrl = cUrl;
    }
    public String getcContent() {
        return cContent;
    }
    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    // end 基础get/set方法
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Chapter{" +
                "cId=" + cId +
                ", bUniquelyIdentifies='" + bUniquelyIdentifies + '\'' +
                ", cNumber=" + cNumber +
                ", cTitle='" + cTitle + '\'' +
                ", cUrl='" + cUrl + '\'' +
                ", cContent='" + cContent + '\'' +
                '}';
    }

    @NonNull
    @Override
    public Chapter clone() {
        try {
            return (Chapter) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new InternalError(e);
        }
    }
}
