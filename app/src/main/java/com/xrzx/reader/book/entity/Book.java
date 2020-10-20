package com.xrzx.reader.book.entity;

import android.widget.TextView;

import java.io.Serializable;

public class Book implements Serializable {
    static final long serialVersionUID = 42L;

    /**
     * 书籍名称
     */
    private String name;
    /**
     * 书籍作者
     */
    private String author;
    /**
     * 书记类型
     */
    private String type;
    /**
     * 书籍简介
     */
    private String introduction;
    /**
     * 书籍地址
     */
    private String bookUrl;
    /**
     * 书籍目录地址
     */
    private String chapterUrl;
    /**
     * 最后更新时间
     */
    private String lastUpdateTime;
    /**
     * 最后更新章节
     */
    private String lastUpdateChapter;

    public Book() {
    }

    public Book(String name, String chapterUrl) {
        this.name = name;
        this.chapterUrl = chapterUrl;
    }

    public Book(String name, String author, String bookUrl, String chapterUrl, String lastUpdateTime, String lastUpdateChapter) {
        this.name = name;
        this.author = author;
        this.bookUrl = bookUrl;
        this.chapterUrl = chapterUrl;
        this.lastUpdateTime = lastUpdateTime;
        this.lastUpdateChapter = lastUpdateChapter;
    }

    public Book(String name, String author, String type, String introduction, String bookUrl, String chapterUrl, String lastUpdateTime, String lastUpdateChapter) {
        this.name = name;
        this.author = author;
        this.type = type;
        this.introduction = introduction;
        this.bookUrl = bookUrl;
        this.chapterUrl = chapterUrl;
        this.lastUpdateTime = lastUpdateTime;
        this.lastUpdateChapter = lastUpdateChapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateChapter() {
        return lastUpdateChapter;
    }

    public void setLastUpdateChapter(String lastUpdateChapter) {
        this.lastUpdateChapter = lastUpdateChapter;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", introduction='" + introduction + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", chapterUrl='" + chapterUrl + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", lastUpdateChapter='" + lastUpdateChapter + '\'' +
                '}';
    }
}
