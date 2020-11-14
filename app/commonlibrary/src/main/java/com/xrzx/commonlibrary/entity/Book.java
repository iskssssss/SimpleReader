package com.xrzx.commonlibrary.entity;

import androidx.annotation.NonNull;

import com.xrzx.commonlibrary.annotation.SQLiteAnnotation;
import com.xrzx.commonlibrary.enums.SQLiteDataType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description 书籍实体类
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class Book implements Serializable, Cloneable {
    private static final long serialVersionUID = 42L;

    /**
     * 书籍序号
     */
    @SQLiteAnnotation(order = 1, dataType = SQLiteDataType.LONG, key = true, autoincrement = true, notNull = true)
    private Long bId;
    /**
     * 书籍唯一标识
     */
    @SQLiteAnnotation(order = 2, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bUniquelyIdentifies;
    /**
     * 书籍名称
     */
    @SQLiteAnnotation(order = 3, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bName;
    /**
     * 书籍作者
     */
    @SQLiteAnnotation(order = 4, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bAuthor;
    /**
     * 书籍类型
     */
    @SQLiteAnnotation(order = 5, dataType = SQLiteDataType.TEXT)
    private String bType;
    /**
     * 书籍简介
     */
    @SQLiteAnnotation(order = 6, dataType = SQLiteDataType.TEXT)
    private String bIntroduction;
    /**
     * 最近阅读日期
     */
    @SQLiteAnnotation(order = 7, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bNewReadTime;
    /**
     * 书籍地址
     */
    @SQLiteAnnotation(order = 8, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bBookUrl;
    /**
     * 书籍目录地址
     */
    @SQLiteAnnotation(order = 9, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bChapterUrl;
    /**
     * 最后更新时间
     */
    @SQLiteAnnotation(order = 10, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bLastUpdateTime;
    /**
     * 最后更新章节
     */
    @SQLiteAnnotation(order = 11, dataType = SQLiteDataType.TEXT, notNull = true)
    private String bLastUpdateChapter;
    /**
     * 当前阅读章节id
     */
    @SQLiteAnnotation(order = 12, dataType = SQLiteDataType.INTEGER)
    private Integer bCurrentReadChapterId;
    /**
     * 当前阅读章节页数
     */
    @SQLiteAnnotation(order = 13, dataType = SQLiteDataType.INTEGER)
    private Integer bCurrentReadChapterPage;
    /**
     * 是否更新
     */
    @SQLiteAnnotation(order = 14, dataType = SQLiteDataType.INTEGER)
    private Integer bUpdate;

    /**
     * 当前书籍是否添加书架
     */
    private boolean bookShelf = false;
    /**
     * 章节列表
     */
    private ArrayList<Chapter> chapterList;

    public Book() {
    }
    public Book(String uniquelyIdentifies, String newReadTime) {
        this.bUniquelyIdentifies = uniquelyIdentifies;
        this.bNewReadTime = newReadTime;
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

    /**
     * 增当前阅读章节id
     *
     * @return  如果是最后章返回 true  反之 false
     */
    public boolean increaseCurrentReadChapterId() {
        if (this.bCurrentReadChapterId == chapterList.size()) {
            return true;
        }
        ++this.bCurrentReadChapterId;
        return false;
    }

    /**
     * 减当前阅读章节id
     *
     * @return 如果是第一章返回 true  反之 false
     */
    public boolean lessCurrentReadChapterId() {
        if (this.bCurrentReadChapterId == 1) {
            return true;
        }
        --this.bCurrentReadChapterId;
        return false;
    }

    public Integer currentReadChapterIdIndex() {
        return bCurrentReadChapterId - 1;
    }

    //---------------------------------------------------------------------------------------------------------------
    // start 基础get/set方法

    public Long getbId() {
        return bId;
    }
    public void setbId(Long bId) {
        this.bId = bId;
    }
    public String getbUniquelyIdentifies() {
        return bUniquelyIdentifies;
    }
    public void setbUniquelyIdentifies(String bUniquelyIdentifies) {
        this.bUniquelyIdentifies = bUniquelyIdentifies;
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
    public String getbNewReadTime() {
        return bNewReadTime;
    }
    public void setbNewReadTime(String bNewReadTime) {
        this.bNewReadTime = bNewReadTime;
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
    public Integer getbCurrentReadChapterId() {
        return bCurrentReadChapterId;
    }
    public void setbCurrentReadChapterId(Integer bCurrentReadChapterId) {
        this.bCurrentReadChapterId = bCurrentReadChapterId;
    }
    public Integer getbCurrentReadChapterPage() {
        return bCurrentReadChapterPage;
    }
    public void setbCurrentReadChapterPage(Integer bCurrentReadChapterPage) {
        this.bCurrentReadChapterPage = bCurrentReadChapterPage;
    }

    public Integer getbUpdate() {
        return bUpdate;
    }

    public void setbUpdate(Integer bUpdate) {
        this.bUpdate = bUpdate;
    }

    public boolean isBookShelf() {
        return bookShelf;
    }
    public void setBookShelf(boolean bookShelf) {
        this.bookShelf = bookShelf;
    }
    public ArrayList<Chapter> getChapterList() {
        return chapterList;
    }
    public void setChapterList(ArrayList<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    // end 基础get/set方法
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Book{" +
                "bId=" + bId +
                ", bUniquelyIdentifies='" + bUniquelyIdentifies + '\'' +
                ", bName='" + bName + '\'' +
                ", bAuthor='" + bAuthor + '\'' +
                ", bType='" + bType + '\'' +
                ", bIntroduction='" + bIntroduction + '\'' +
                ", bNewReadTime='" + bNewReadTime + '\'' +
                ", bBookUrl='" + bBookUrl + '\'' +
                ", bChapterUrl='" + bChapterUrl + '\'' +
                ", bLastUpdateTime='" + bLastUpdateTime + '\'' +
                ", bLastUpdateChapter='" + bLastUpdateChapter + '\'' +
                ", bCurrentReadChapterId=" + bCurrentReadChapterId +
                ", bCurrentReadChapterPage=" + bCurrentReadChapterPage +
                ", bookShelf=" + bookShelf +
                ", chapterList=" + chapterList +
                '}';
    }

    @NonNull
    @Override
    public Book clone() {
        try {
            return (Book) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new InternalError(e);
        }
    }
}
