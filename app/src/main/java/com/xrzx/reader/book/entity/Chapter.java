package com.xrzx.reader.book.entity;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @Description 章节信息实体类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class Chapter implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 章节id
     */
    private Logger cId;
    /**
     * 书籍编号
     */
    private Long bId;
    /**
     * 章节序号
     */
    private Integer cNumber;
    /**
     * 章节标题
     */
    private String cTitle;
    /**
     * 章节链接
     */
    private String cUrl;
    /**
     * 章节正文
     */
    private String cContent;

    public Chapter(String cTitle, String cUrl) {
        this.cTitle = cTitle;
        this.cUrl = cUrl;
    }

    public Chapter(Integer cNumber, String cTitle, String cUrl) {
        this.cNumber = cNumber;
        this.cTitle = cTitle;
        this.cUrl = cUrl;
    }

    public Logger getcId() {
        return cId;
    }

    public void setcId(Logger cId) {
        this.cId = cId;
    }

    public Long  getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
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

    @Override
    public String toString() {
        return "ChapterInfo{" +
                "bId='" + bId + '\'' +
                ", cNumber=" + cNumber +
                ", cTitle='" + cTitle + '\'' +
                ", cUrl='" + cUrl + '\'' +
                ", cContent='" + cContent + '\'' +
                '}';
    }
}
