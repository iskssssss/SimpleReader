package com.xrzx.reader.activity.base;

import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.commonlibrary.utils.ThreadUtils;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.book.http.BookHttpApi;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/29 22:25
 */
public class BaseActivity extends AppCompatActivity {
    protected final static ExecutorService OTHER_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getOtherExecutorServiceThreadPool();
    protected final static ExecutorService CRAWLING_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getCrawlingExecutorServiceThreadPool();
    protected final static GlobalData GLOBAL_DATA = GlobalData.getInstance();

    protected static Book book;
    protected static ArrayList<Chapter> chapterList;

    protected static int width = -1;
    protected static int height = -1;

    /**
     * 预加载章节数（选择章节的前）
     */
    private static final int PRELOADED_CHAPTERS_NUMBER_BEFORE = 1;
    /**
     * 预加载章节数（选择章节的后）
     */
    private static final int PRELOADED_CHAPTERS_NUMBER_AFTER = 3;

    protected static final int SELECT_CHAPTER_CODE = 1;

    protected Message getMessage(int what) {
        return getMessage(what, null);
    }

    protected Message getMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }

    /**
     * 获取书籍信息
     *
     * @return
     */
    protected void getBookInfo() {
        if (width == -1 || height == -1) {
            width = AndroidUtils.getWidth(this);
            height = AndroidUtils.getHeight(this);
        }

        if (null != book && book.getbUniquelyIdentifies().equals(GLOBAL_DATA.getCurrSelectBook().getbUniquelyIdentifies())) {
            return;
        }
        book = GLOBAL_DATA.getCurrSelectBook();

        if (null == book.getChapterList()) {
            book.setChapterList(new ArrayList<>());
        }
        chapterList = book.getChapterList();
    }

    Book updateBook = new Book();

    /**
     * 储存图书阅读信息
     *
     * @param currentReadChapterId   阅读章节id
     * @param currentReadChapterPage 阅读章节页数
     */
    protected void updateReadingRecord(int currentReadChapterId, int currentReadChapterPage) {
        book.setbCurrentReadChapterPage(currentReadChapterPage);
        if (!book.isBookShelf()) {
            return;
        }
        updateBook.setbUniquelyIdentifies(book.getbUniquelyIdentifies());
        if (currentReadChapterId != -1) {
            updateBook.setbCurrentReadChapterId(currentReadChapterId);
        }
        updateBook.setbCurrentReadChapterPage(currentReadChapterPage);
        BookInfoDao.updateBook(updateBook);
        updateBook.setbCurrentReadChapterId(null);
        updateBook.setbCurrentReadChapterPage(null);
    }

    /**
     * 获取书籍目录
     *
     * @param book        书籍
     * @param chapterList 目录存放容器
     * @param getContent  是否获取正文
     * @param handler     通知
     */
    protected void getChapters(Book book, ArrayList<Chapter> chapterList, boolean getContent, Handler handler, Message msg) {
        ArrayList<Chapter> newChapterList = new ArrayList<>();
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> BookHttpApi.getBookChapters(book, newChapterList, new ResultCallBack<ArrayList<Chapter>>() {
            @Override
            public void onSuccess(ArrayList<Chapter> result) {
                if (chapterList.size() == 0) {
                    chapterList.addAll(newChapterList);
                } else if (newChapterList.size() != chapterList.size()) {
                    for (int i = chapterList.size(); i < newChapterList.size(); i++) {
                        final Chapter chapter = newChapterList.get(i);
                        chapterList.add(chapter);
                        if (!book.isBookShelf()) {
                            continue;
                        }
                        ChapterInfoDao.writeChapter(chapter);
                    }
                }
                if (getContent) {
                    getChapterContent(true, false);
                }
                if (null != handler) {
                    msg.obj = newChapterList.size() != chapterList.size();
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                ToastUtils.show("书籍目录获取失败，请稍后尝试。");
            }
        }));
    }


    protected void getChapterContent(boolean headDraw, boolean thread) {
        getChapterContent(null, null, headDraw, thread);
    }


    /**
     * 获取章节正文内容
     */
    protected void getChapterContent(Handler handler, Message message, boolean headDraw, boolean thread) {
        if (thread) {
            CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> getContent(handler, message, headDraw));
            return;
        }
        getContent(handler, message, headDraw);
    }

    protected void getContent(Handler handler, Message message, boolean headDraw) {
        final int currentReadChapterId = book.currentReadChapterIdIndex();
        int startIndex = currentReadChapterId - PRELOADED_CHAPTERS_NUMBER_BEFORE;
        int endIndex = currentReadChapterId + PRELOADED_CHAPTERS_NUMBER_AFTER;
        if (startIndex <= -1) {
            startIndex = currentReadChapterId;
        }
        if (endIndex >= chapterList.size()) {
            endIndex = chapterList.size() - 1;
        }
        // 判断当前选择的章节内容是否为空  为空则获取内容
        if (null == chapterList.get(currentReadChapterId).getcContent()) {
            getContent(chapterList.get(currentReadChapterId));
        }
        if (null != handler) {
            message.obj = headDraw;
            handler.sendMessage(message);
        }
        for (int i = startIndex; i <= endIndex; i++) {
            Chapter chapter = chapterList.get(i);
            boolean isContinue = null != chapter.getcContent() && !"".equals(chapter.getcContent()) || i == currentReadChapterId;
            if (isContinue) {
                continue;
            }
            getContent(chapter);
        }
    }

    private void getContent(final Chapter chapter) {
        BookHttpApi.getBookChapterContent(chapter, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                chapter.setcContent(result);
                if (book.isBookShelf()) {
                    ChapterInfoDao.updateChapterContent(String.valueOf(chapter.getcId()), result);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtils.show("[" + chapter.getcTitle() + "]内容获取失败。");
                e.printStackTrace();
            }
        });
    }
}
