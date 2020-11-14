package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.GlobalData;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.base.BaseActivity;
import com.xrzx.reader.adapter.ChapterAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * @Description 目录页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ChaptersActivity extends BaseActivity implements View.OnClickListener {
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ToastUtils.show("目录加载成功.");
                if (currentBook.isBookShelf() && ChapterInfoDao.isExistChapters(currentBook)) {
                    ChapterInfoDao.writeChapters(currentChapterList);
                }
                chapterAdapter.notifyDataSetChanged();
            }
        }
    };

    private ListView listView;
    private ChapterAdapter chapterAdapter;

    private LinearLayout llSort;
    private ImageView ivSortImg;

    private void initView() {
        findViewById(R.id.ac_ll_back).setOnClickListener(this);

        llSort = findViewById(R.id.ac_ll_sort);
        llSort.setOnClickListener(this);
        llSort.setTag(true);
        ivSortImg = findViewById(R.id.ac_iv_sort_img);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalData globalData = GlobalData.getInstance();
        if (globalData.readPageSettingLog.gAtNight()){
            setTheme(R.style.AppThemeNight);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_chapters);

        initView();


        final boolean isChapters = getIntent().getBooleanExtra("isChapters", true);
        ToastUtils.show("正在加载目录...");
        if (isChapters) {
            setCurrentBook();
            if (currentChapterList.isEmpty()) {
                ChapterInfoDao.readChapters(currentBook, currentChapterList);
                getChapters(currentBook, currentChapterList, false, handler, getMessage(1));
            }
        } else {
            OTHER_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
                while (currentChapterList.isEmpty()) {
                }
                handler.sendMessage(getMessage(1));
            });
        }

        chapterAdapter = new ChapterAdapter(ChaptersActivity.this, R.layout.item_chapter, currentChapterList);
        chapterAdapter.setItemSelectChapterIndex(currentBook.getbCurrentReadChapterId());
        listView = findViewById(R.id.fs_lv_book_shelf);
        listView.setAdapter(chapterAdapter);

        // 将书籍目录定位至当前观看的章节位置
        final int itemChapterHeight = AndroidUtils.sp2px(this, 56f);
        final float screenHeight = (float) height - AndroidUtils.sp2px(this, 48f);
        int visibleListNumber = (int) Math.ceil((screenHeight / itemChapterHeight) / 2f);
        if (currentBook.getbCurrentReadChapterId() > visibleListNumber) {
            listView.setSelection(currentBook.getbCurrentReadChapterId() - visibleListNumber);
        }
        listView.setOnItemClickListener((parent, view, position, id) -> {
            currentBook.setbCurrentReadChapterId(position + 1);
            chapterAdapter.setItemSelectChapterIndex(currentBook.getbCurrentReadChapterId());
            updateReadingRecord(currentBook.getbCurrentReadChapterId(), 1);
            Intent intent = new Intent(ChaptersActivity.this, ReadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            setResult(SELECT_CHAPTER_CODE, intent);

            startActivity(intent);
            ChaptersActivity.this.finish();
        });

        View btnUpdate = findViewById(R.id.ac_btnUpdate);
        btnUpdate.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ac_ll_sort:
                if ((boolean) llSort.getTag()) {
                    listView.setSelection(currentChapterList.size() - 1);
                    setSortStyle(R.drawable.ico_sort_asc, false);
                } else {
                    listView.setSelection(0);
                    setSortStyle(R.drawable.ico_sort_desc, true);
                }
                break;
            case R.id.ac_ll_back:
                ChaptersActivity.this.finish();
                break;
            case R.id.ac_btnUpdate:
                ToastUtils.show("正在刷新目录...");
                getChapters(currentBook, currentChapterList, false, handler, getMessage(1));
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    private void setSortStyle(int backgroundResource, boolean tag) {
        ivSortImg.setBackgroundResource(backgroundResource);
        llSort.setTag(tag);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChaptersActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}