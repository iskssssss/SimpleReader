package com.xrzx.reader;

import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.utils.DateUtils;
import com.xrzx.reader.view.adapter.BookShelfListAdapter;

import java.util.ArrayList;

/**
 * @Description 全局数据
 * @Author ks
 * @Date 2020/11/4 19:45
 */
public class GlobalData {
    private static volatile GlobalData globalData = null;

    public static GlobalData getInstance() {
        if (globalData == null) {
            synchronized (GlobalData.class) {
                if (globalData == null) {
                    globalData = new GlobalData();
                }
            }
        }
        return globalData;
    }

    private GlobalData() {
        bookShelfList = new ArrayList<>();
    }


    /**
     * 书架适配器
     */
    private BookShelfListAdapter chapterAdapter;

    public void notifyDataSetChanged() {
        this.chapterAdapter.notifyDataSetChanged();
    }

    public void setChapterAdapter(BookShelfListAdapter chapterAdapter) {
        this.chapterAdapter = chapterAdapter;
    }

    public BookShelfListAdapter getChapterAdapter() {
        return chapterAdapter;
    }
    // ---------------------------------------------------------------------------------------------------------
    /**
     * 书架书籍信息
     */
    private final ArrayList<Book> bookShelfList;

    /**
     * 判断书籍是否在书架
     *
     * @param book 书籍
     * @return 存在返回书籍信息 反之 null
     */
    public Book searchBook(Book book) {
        if (null == book) {
            return null;
        }
        for (Book book1 : bookShelfList) {
            final String name = book1.getbName();
            final String author = book1.getbAuthor();
            final String bookUrl = book1.getbBookUrl();
            if (name.equals(book.getbName())
                    && author.equals(book.getbAuthor())
                    && bookUrl.equals(book.getbBookUrl())) {
                return book1;
            }
        }
        return null;
    }

    /**
     * 获取书籍所在行数
     *
     * @param book 书籍
     * @return Position
     */
    public int getPosition(Book book) {
        return bookShelfList.indexOf(book);
    }

    public void putBook(Book book) {
        if (null == book) {
            return;
        }
        book.setbNewReadTime(DateUtils.getDateTime());
        book.setChapterList(new ArrayList<>());
        book.setBookShelf(true);
        BookInfoDao.writeEntity(book);
        bookShelfList.add(0, book);
        currSelectPosition = 0;
        notifyDataSetChanged();
    }

    public void removeBook(int position) {
        if (position < 0 || position > (bookShelfList.size() - 1)) {
            return;
        }
        final Book book = bookShelfList.get(position);
        removeBook(book);
    }

    public void removeBook(Book book) {
        if (null == book) {
            return;
        }
        ChapterInfoDao.deleteChapters(book);
        BookInfoDao.deleteEntity(book);
        if (null != book.getChapterList()) {
            book.getChapterList().clear();
            book.setChapterList(null);
        }
        book.setBookShelf(false);
        removeSelectBook();
        bookShelfList.remove(book);
        notifyDataSetChanged();
    }

    public void setBookShelfList(ArrayList<Book> bookShelfList) {
        this.bookShelfList.clear();
        // 将书籍设置为在书架 和 实例化章节列表
        bookShelfList.forEach(book -> {
            book.setBookShelf(true);
            book.setChapterList(new ArrayList<>());
        });
        this.bookShelfList.addAll(bookShelfList);
        notifyDataSetChanged();
    }

    public ArrayList<Book> getBookShelfList() {
        return bookShelfList;
    }

    /**
     * 将书籍放在第一位置
     */
    public void putBookFirst() {
        bookShelfList.remove(currSelectPosition);
        bookShelfList.add(0, currSelectBook);
        notifyDataSetChanged();
    }

    /**
     * 将书籍放在第一位置
     *
     * @param book     书籍
     * @param position 书籍所在位置
     */
    public void putBookFirst(Book book, int position) {
        bookShelfList.remove(position);
        bookShelfList.add(0, book);
        notifyDataSetChanged();
    }
    // ---------------------------------------------------------------------------------------------------------
    /**
     * 当前选择的书籍
     */
    private Book currSelectBook;

    public Book getCurrSelectBook() {
        return currSelectBook;
    }

    /**
     * 当前选择的书籍位置
     */
    private int currSelectPosition;

    public int getCurrSelectPosition() {
        return currSelectPosition;
    }

    /**
     * 设置当前选择的书籍和书籍位置
     *
     * @param book     书籍
     * @param position 书籍所在位置
     */
    public void setCurrSelectBookAndPosition(Book book, int position) {
        setCurrSelectBookAndPosition(book, position, false);
    }

    /**
     * 设置当前选择的书籍和书籍位置
     *
     * @param book         书籍
     * @param position     书籍所在位置
     * @param putBookFirst 是否将书籍放在第一位
     */
    public void setCurrSelectBookAndPosition(Book book, int position, boolean putBookFirst) {
        this.currSelectBook = book;
        this.currSelectPosition = position;
        if (position == -1 || !putBookFirst) {
            return;
        }
        this.putBookFirst();
    }

    public void removeSelectBook() {
        this.currSelectBook = null;
        this.currSelectPosition = -1;
    }
    // ---------------------------------------------------------------------------------------------------------
}
