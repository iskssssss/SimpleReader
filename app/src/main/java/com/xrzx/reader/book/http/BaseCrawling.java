package com.xrzx.reader.book.http;

import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/12 11:15
 */
public interface BaseCrawling {
    /**
     * 搜索图书信息
     *
     * @param searchDict 搜索字典
     * @param bookList   图书列表
     * @param callBack   回调
     */
    void searchBooks(final String searchKey, final ArrayList<Book> bookList, final ResultCallBack<List<Book>> callBack);

    /**
     * 获取书籍详细信息
     *
     * @param book     书籍信息
     * @param callBack 回调
     */
    void getBookDetailsInfo(final Book book, final ResultCallBack<Book> callBack);

    /**
     * 获取书籍详细信息
     *
     * @param book     书籍信息
     * @param callBack 回调
     */
    void updateBookDetailsInfo(final Book book, final ResultCallBack<Book> callBack);

    /**
     * 获取目录信息
     *
     * @param book        书籍信息
     * @param chapterList 目录列表
     * @param callBack    回调
     */
    void getBookChapters(final Book book, final ArrayList<Chapter> chapterList, final ResultCallBack<ArrayList<Chapter>> callBack);

    /**
     * 获取章节正文
     *
     * @param chapter  章节
     * @param callBack 回调
     */
    void getBookChapterContent(final Chapter chapter, final ResultCallBack<String> callBack);
}
