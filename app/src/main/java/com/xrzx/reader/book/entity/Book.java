package com.xrzx.reader.book.entity;

import java.io.Serializable;

/**
 * @Description 书籍实体类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 书籍编号
     */
    private Long bId;

    /**
     * 书籍名称
     */
    private String bName;

    /**
     * 书籍作者
     */
    private String bAuthor;

    /**
     * 书记类型
     */
    private String bType;

    /**
     * 书籍简介
     */
    private String bIntroduction;

    /**
     * 书籍地址
     */
    private String bBookUrl;

    /**
     * 书籍目录地址
     */
    private String bChapterUrl;

    /**
     * 最后更新时间
     */
    private String bLastUpdateTime;

    /**
     * 最后更新章节
     */
    private String bLastUpdateChapter;

    /**
     * 当前阅读章节id
     */
    private Long bCurrentReadChapterId;

    public Book() {
    }

    public Book(String name, String chapterUrl) {
        this.bName = name;
        this.bChapterUrl = chapterUrl;
    }

    public Book(String name, String author, String bookUrl, String chapterUrl, String lastUpdateTime, String lastUpdateChapter) {
        this.bName = name;
        this.bAuthor = author;
        this.bBookUrl = bookUrl;
        this.bChapterUrl = chapterUrl;
        this.bLastUpdateTime = lastUpdateTime;
        this.bLastUpdateChapter = lastUpdateChapter;
    }

    public Book(String name, String author, String type, String introduction, String bookUrl, String chapterUrl, String lastUpdateTime, String lastUpdateChapter) {
        this.bName = name;
        this.bAuthor = author;
        this.bType = type;
        this.bIntroduction = introduction;
        this.bBookUrl = bookUrl;
        this.bChapterUrl = chapterUrl;
        this.bLastUpdateTime = lastUpdateTime;
        this.bLastUpdateChapter = lastUpdateChapter;
    }

    public Long getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    public void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public String getbIntroduction() {
        return bIntroduction;
    }

    public void setbIntroduction(String bIntroduction) {
        this.bIntroduction = bIntroduction;
    }

    public String getbBookUrl() {
        return bBookUrl;
    }

    public void setbBookUrl(String bBookUrl) {
        this.bBookUrl = bBookUrl;
    }

    public String getbChapterUrl() {
        return bChapterUrl;
    }

    public void setbChapterUrl(String bChapterUrl) {
        this.bChapterUrl = bChapterUrl;
    }

    public String getbLastUpdateTime() {
        return bLastUpdateTime;
    }

    public void setbLastUpdateTime(String bLastUpdateTime) {
        this.bLastUpdateTime = bLastUpdateTime;
    }

    public String getbLastUpdateChapter() {
        return bLastUpdateChapter;
    }

    public void setbLastUpdateChapter(String bLastUpdateChapter) {
        this.bLastUpdateChapter = bLastUpdateChapter;
    }

    public Long getbCurrentReadChapterId() {
        return bCurrentReadChapterId;
    }

    public void setbCurrentReadChapterId(Long bCurrentReadChapterId) {
        this.bCurrentReadChapterId = bCurrentReadChapterId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bId=" + bId +
                ", bName='" + bName + '\'' +
                ", bAuthor='" + bAuthor + '\'' +
                ", bType='" + bType + '\'' +
                ", bIntroduction='" + bIntroduction + '\'' +
                ", bBookUrl='" + bBookUrl + '\'' +
                ", bChapterUrl='" + bChapterUrl + '\'' +
                ", bLastUpdateTime='" + bLastUpdateTime + '\'' +
                ", bLastUpdateChapter='" + bLastUpdateChapter + '\'' +
                ", currentReadChapterId=" + bCurrentReadChapterId +
                '}';
    }
}
