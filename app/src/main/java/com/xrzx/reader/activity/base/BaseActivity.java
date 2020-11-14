package com.xrzx.reader.activity.base;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

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
import com.xrzx.reader.book.http.BaseCrawling;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/29 22:25
 */
public class BaseActivity extends AppCompatActivity {
    protected final static ThreadPoolExecutor OTHER_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getOtherExecutorServiceThreadPool();
    protected final static ThreadPoolExecutor CRAWLING_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getCrawlingExecutorServiceThreadPool();
    protected final static GlobalData GLOBAL_DATA = GlobalData.getInstance();
    protected final BaseCrawling crawlingApi = GLOBAL_DATA.getCrawlingApi();

    protected static boolean isWhirling = true;

    /**
     * 当前阅读书籍
     */
    protected static Book currentBook;
    /**
     * 当前阅读的书籍目录
     */
    protected static ArrayList<Chapter> currentChapterList;

    protected static int width = -1;
    protected static int height = -1;

    protected final static Book UPDATE_BOOK = new Book();

    /**
     * 预加载章节数（选择章节的前）
     */
    private static final int PRELOADED_CHAPTERS_NUMBER_BEFORE = 1;
    /**
     * 预加载章节数（选择章节的后）
     */
    private static final int PRELOADED_CHAPTERS_NUMBER_AFTER = 3;
    /**
     * 在阅读界面进入目录时的判断代码
     */
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
     * 设置当前查阅书籍信息
     */
    protected void setCurrentBook() {
        // 获取手机的宽高
        if (width == -1 || height == -1) {
            width = AndroidUtils.getWidth(this);
            height = AndroidUtils.getHeight(this);
        }
        if (null != currentBook && currentBook.isBookShelf() && GLOBAL_DATA.getCurrSelectBook().isBookShelf()) {
            if (currentBook.getbUniquelyIdentifies().equals(GLOBAL_DATA.getCurrSelectBook().getbUniquelyIdentifies())) {
                return;
            }
        }
        currentBook = GLOBAL_DATA.getCurrSelectBook();
        currentChapterList = currentBook.getChapterList();
    }

    /**
     * 储存图书阅读信息
     *
     * @param currentReadChapterId   阅读章节id
     * @param currentReadChapterPage 阅读章节页数
     */
    protected void updateReadingRecord(int currentReadChapterId, int currentReadChapterPage) {
        currentBook.setbCurrentReadChapterPage(currentReadChapterPage);
        if (!currentBook.isBookShelf()) {
            return;
        }
        UPDATE_BOOK.setbUniquelyIdentifies(currentBook.getbUniquelyIdentifies());
        if (currentReadChapterId != -1) {
            UPDATE_BOOK.setbCurrentReadChapterId(currentReadChapterId);
        }
        UPDATE_BOOK.setbCurrentReadChapterPage(currentReadChapterPage);
        BookInfoDao.updateBook(UPDATE_BOOK);
        UPDATE_BOOK.setbCurrentReadChapterId(null);
        UPDATE_BOOK.setbCurrentReadChapterPage(null);
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
        CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> crawlingApi.getBookChapters(book, newChapterList, new ResultCallBack<ArrayList<Chapter>>() {
            @Override
            public void onSuccess(ArrayList<Chapter> result) {
                if (chapterList.size() == 0) {
                    chapterList.addAll(newChapterList);
                    if (currentBook.isBookShelf()) {
                        ChapterInfoDao.writeChapters(chapterList);
                    }
                } else if (newChapterList.size() != chapterList.size()) {
                    for (int i = chapterList.size(); i < newChapterList.size(); i++) {
                        final Chapter chapter = newChapterList.get(i);
                        chapterList.add(chapter);
                        if (!book.isBookShelf()) {
                            continue;
                        }
                        if (currentBook.isBookShelf()) {
                            ChapterInfoDao.writeChapter(chapter);
                        }
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
            CRAWLING_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> getContentAndUpdate(handler, message, headDraw));
            return;
        }
        getContentAndUpdate(handler, message, headDraw);
    }

    protected void getContentAndUpdate(Handler handler, Message message, boolean headDraw) {
        final int currentReadChapterId = currentBook.currentReadChapterIdIndex();
        int startIndex = currentReadChapterId - PRELOADED_CHAPTERS_NUMBER_BEFORE;
        int endIndex = currentReadChapterId + PRELOADED_CHAPTERS_NUMBER_AFTER;
        if (startIndex <= -1) {
            startIndex = currentReadChapterId;
        }
        if (endIndex >= currentChapterList.size()) {
            endIndex = currentChapterList.size() - 1;
        }
        // 判断当前选择的章节内容是否为空  为空则获取内容
        if (null == currentChapterList.get(currentReadChapterId).getcContent()) {
            getContentAndUpdate(currentChapterList.get(currentReadChapterId));
        }
        if (null != handler) {
            message.obj = headDraw;
            handler.sendMessage(message);
        }
        for (int i = startIndex; i <= endIndex; i++) {
            Chapter chapter = currentChapterList.get(i);
            boolean isContinue = null != chapter.getcContent() && !"".equals(chapter.getcContent()) || i == currentReadChapterId;
            if (isContinue) {
                continue;
            }
            getContentAndUpdate(chapter);
        }
    }

    /**
     * 获取文章内容
     *
     * @param chapter 章节信息
     */
    protected void getContentAndUpdate(final Chapter chapter) {
        crawlingApi.getBookChapterContent(chapter, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                chapter.setcContent(result);
                // 如果当前书籍在书架的话 更新当前章节的信息
                if (currentBook.isBookShelf()) {
                    ChapterInfoDao.updateChapterContent(String.valueOf(chapter.getcId()), result);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtils.show("[" + chapter.getcTitle() + "] 内容获取失败。");
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取文章内容
     *
     * @param chapter 章节信息
     */
    protected void getContent(final Chapter chapter) {
        crawlingApi.getBookChapterContent(chapter, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                chapter.setcContent(result);
            }

            @Override
            public void onError(Exception e) {
                ToastUtils.show("[" + chapter.getcTitle() + "] 内容获取失败。");
                e.printStackTrace();
            }
        });
    }

    /**
     * 系统亮度
     */
    private static int systemBrightness = -1;


    /**
     * 设置屏幕亮度
     *
     * @param brightness        亮度
     */
    public void setBrightness(Window window, float brightness) {
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
            lp.screenBrightness = brightness;
        } else {
            lp.screenBrightness = brightness * (1f / 255f);
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 获取屏幕亮度
     *
     * @return 亮度
     */
    public int getScreenBrightness(ContentResolver cr) {
        try {
            return Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            return 0;
        }
    }

    /**
     * 是否开启自动亮度
     *
     * @return
     */
    public boolean isAutoBrightness(ContentResolver cr) {
        try {
            return Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 开启自动亮度
     */
    public void autoBrightness(Context context, ContentResolver cr) {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
    }

    /**
     * 关闭自动亮度
     */
    public void stopBrightness(Context context, ContentResolver cr) {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
    }
}
