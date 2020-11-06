package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.base.BaseActivity;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.callback.ResultCallBack;
import com.xrzx.commonlibrary.enums.TouchMoveDirection;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.view.custom.ChapterContentTextView;

import org.jetbrains.annotations.NotNull;

/**
 * @Description 阅读页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ReadActivity extends BaseActivity {

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initBookInfo();
                    break;
                case 2:
                    final Chapter chapter = chapterList.get(book.currentReadChapterIdIndex());
                    content.stopDrawLoadingInfo();
                    content.setChapterInfo(chapter.getcTitle(), chapter.getcContent(), 1, true, ((boolean) msg.obj));
                    content.startDrawContentInfo();
                    break;
                case 3:
                    if ((boolean) msg.obj) {
                        book.increaseCurrentReadChapterId();
                    } else {
                        ToastUtils.show("当前已是最新章节.");
                    }
                    content.stopDrawLoadingInfo();
                    content.startDrawContentInfo();
                    break;
                default:
                    System.out.println("handleMessage.default");
                    break;
            }
        }
    };

    private ChapterContentTextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read);

        initView();
        getBookInfo();
        initCatalogInfo();
        // 处理用户翻页
        content.setCallBack(new ResultCallBack<Object>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onError(Exception e) {

            }
        });

        // 判断当前书籍是否是在书架 在则将书籍放置第一栏
        if (book.isBookShelf()) {
            GLOBAL_DATA.putBookFirst();
        }
    }

    /**
     * 初始化图书正文信息
     */
    private void initBookInfo() {
        final Chapter chapter = chapterList.get(book.currentReadChapterIdIndex());
        content.stopDrawLoadingInfo();
        content.setChapterInfo(chapter.getcTitle(), chapter.getcContent(), book.getbCurrentReadChapterPage());
        content.startDrawContentInfo();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        content = findViewById(R.id.arn_tVContent);
    }

    /**
     * 初始化书籍目录信息
     */
    private void initCatalogInfo() {
        content.startDrawLoadingInfo();
        if (!chapterList.isEmpty()) {
            initBookInfo();
            return;
        }
        ChapterInfoDao.readChapters(book, chapterList);

        if (book.getChapterList().isEmpty()) {
            getChapters(book, book.getChapterList(), true, handler, getMessage(1));
        } else {
            final Chapter chapter = chapterList.get(book.currentReadChapterIdIndex());
            if (null == chapter.getcContent()) {
                getChapterContent(handler, getMessage(1), true, true);
                return;
            }
            content.stopDrawLoadingInfo();
            initBookInfo();
        }
    }

    /**
     * 设置章节信息
     *
     * @param headDraw 是否从头部开始绘制
     */
    private void setChapterContentInfo(boolean headDraw) {
        content.stopDrawContentInfo();
        content.startDrawLoadingInfo();
        getChapterContent(handler, getMessage(2), headDraw, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldEventX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                switch (touchMoveDirection(event.getX(), event.getY())) {
                    case LEFT:
                        nextPage();
                        break;
                    case RIGHT:
                        prevPage();
                        break;
                    case MIDDLE:
                        showSetting();
                        break;
                    default:
                        System.out.println("default");
                        break;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    Dialog dialog = null;
    @SuppressLint("ClickableViewAccessibility")
    private void showSetting() {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.default_dialog_style);
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog_read_setting, null);
            view.findViewById(R.id.drs_llTopMenu).setOnClickListener(null);
            view.findViewById(R.id.drs_llButtonMenu).setOnClickListener(null);
            // 返回
            final TextView tvBack = view.findViewById(R.id.drs_tvBack);
            tvBack.setOnClickListener(v -> {
                ToastUtils.show("返回");
                System.out.println("返回");
            });
            // 设置标题
            final TextView tvChapterName = view.findViewById(R.id.drs_tvChapterName);
            final Chapter chapter = chapterList.get(book.currentReadChapterIdIndex());
            tvChapterName.setText(chapter.getcTitle());
            // 更多
            final TextView tvMore = view.findViewById(R.id.drs_tvMore);
            tvMore.setOnClickListener(v -> {
                ToastUtils.show("更多");
                System.out.println("更多");
            });
            // 上一章
            final TextView tvPrevChapter = view.findViewById(R.id.drs_tvPrevChapter);
            tvPrevChapter.setOnClickListener(v -> {
                prevPage();
                ToastUtils.show("上一章");
                ToastUtils.show("上一章");
            });
            // 下一章
            final TextView tvNextChapter = view.findViewById(R.id.drs_tvNextChapter);
            tvNextChapter.setOnClickListener(v -> {
                nextPage();
                ToastUtils.show("下一章");
                System.out.println("下一章");
            });
            // 目录
            final LinearLayout llCatalogItem = view.findViewById(R.id.drs_llCatalogItem);
            llCatalogItem.setOnClickListener(v -> {
                Intent intent = new Intent(ReadActivity.this, ChaptersActivity.class);
                intent.putExtra("isChapters", false);
                startActivityForResult(intent, SELECT_CHAPTER_CODE);
                dialog.dismiss();
                System.out.println("目录");
            });
            // 缓存
            final LinearLayout llDownloadItem = view.findViewById(R.id.drs_llDownloadItem);
            llDownloadItem.setOnClickListener(v -> {
                ToastUtils.show("缓存");
                System.out.println("缓存");
            });
            // 夜间
            final LinearLayout llNightItem = view.findViewById(R.id.drs_llNightItem);
            llNightItem.setOnClickListener(v -> {
                ToastUtils.show("夜间");
                System.out.println("夜间");
            });
            // 设置
            final LinearLayout llSettingItem = view.findViewById(R.id.drs_llSettingItem);
            llSettingItem.setOnClickListener(v -> {
                ToastUtils.show("设置");
                System.out.println("设置");
            });

            dialog.setContentView(view);
            Window window = dialog.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            view.setOnTouchListener((v, e) -> {
                System.out.println("dialog - public boolean onTouchEvent(MotionEvent event)");
                dialog.dismiss();
                return false;
            });
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.show();
    }

    /**
     * 上一页
     */
    private void prevPage() {
        if (content.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }
        if (!content.prevPage()) {
            updateReadingRecord(-1, content.getCurrentPage());
            return;
        }
        if (!book.lessCurrentReadChapterId()) {
            ToastUtils.show("当前已是第一页.");
            return;
        }
        setChapterContentInfo(false);
        updateReadingRecord(book.getbCurrentReadChapterId(), 1);
    }

    /**
     * 下一页
     */
    protected void nextPage() {
        if (content.isDrawLoadingInfo()) {
            ToastUtils.show("正在加载章节...");
            return;
        }
        if (!content.nextPage()) {
            updateReadingRecord(-1, content.getCurrentPage());
            return;
        }
        if (!book.increaseCurrentReadChapterId()) {
            content.stopDrawContentInfo();
            content.startDrawLoadingInfo("正在获取最新章节...");
            getChapters(book, chapterList, true, handler, getMessage(3));
            return;
        }
        setChapterContentInfo(true);
        updateReadingRecord(book.getbCurrentReadChapterId(), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CHAPTER_CODE && resultCode == SELECT_CHAPTER_CODE) {
            content.stopDrawContentInfo();
            content.startDrawLoadingInfo();
            getChapterContent(handler, getMessage(1), true, true);
        }
    }


    private float oldEventX;

    /**
     * 获取触碰位置
     *
     * @param x x
     * @param y y
     * @return 位置
     */
    public TouchMoveDirection touchMoveDirection(float x, float y) {
        float diff = 1e-6f;
        float height21 = height / 2.0f;
        float height31 = height21 / 3.0f;
        float width21 = width / 2.0f;
        float width31 = width21 / 3.0f;

        if (Math.abs(x - oldEventX) < diff) {
            boolean mid = (x >= width21 - width31 && x <= width21 + width31) &&
                    (y >= height21 - height31 && y <= height21 + height31);
            if (mid) {
                return TouchMoveDirection.MIDDLE;
            }
            if (oldEventX > width21) {
                return TouchMoveDirection.LEFT;
            }
        } else if (x - oldEventX < 0.0) {
            return TouchMoveDirection.LEFT;
        }
        return TouchMoveDirection.RIGHT;
    }
}