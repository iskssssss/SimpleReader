package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xrzx.commonlibrary.database.dao.ChapterInfoDao;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.commonlibrary.utils.ToastUtils;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.base.BaseActivity;
import com.xrzx.reader.view.adapter.ChapterAdapter;

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
                if (book.isBookShelf() && ChapterInfoDao.isExistChapters(book)) {
                    ChapterInfoDao.writeChapters(chapterList);
                }
                chapterAdapter.notifyDataSetChanged();
            }
        }
    };

    private ListView listView;
    private ChapterAdapter chapterAdapter;
    private Button btnSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        final boolean isChapters = getIntent().getBooleanExtra("isChapters", true);

        ToastUtils.show("正在加载目录...");
        if (isChapters) {
            getBookInfo();
            if (chapterList.isEmpty()) {
                ChapterInfoDao.readChapters(book, chapterList);
                getChapters(book, chapterList, false, handler, getMessage(1));
            }
        } else {
            OTHER_EXECUTOR_SERVICE_THREAD_POOL.submit(() -> {
                while (chapterList.isEmpty()) {
                }
                handler.sendMessage(getMessage(1));
            });
        }

        chapterAdapter = new ChapterAdapter(ChaptersActivity.this, R.layout.chapter_item, chapterList);
        chapterAdapter.setItemSelectChapterIndex(book.getbCurrentReadChapterId());
        listView = findViewById(R.id.listView);
        listView.setAdapter(chapterAdapter);

        // 将书籍目录定位至当前观看的章节位置
        int visibleListNumber = (int) Math.ceil(((height - AndroidUtils.sp2px(this, 48f)) / 150f) / 2f);
        if (book.getbCurrentReadChapterId() > visibleListNumber) {
            listView.setSelection(book.getbCurrentReadChapterId() - visibleListNumber);
        }
        listView.setOnItemClickListener((parent, view, position, id) -> {
            book.setbCurrentReadChapterId(position + 1);
            chapterAdapter.setItemSelectChapterIndex(book.getbCurrentReadChapterId());
            updateReadingRecord(book.getbCurrentReadChapterId(), 1);
            Intent intent = new Intent(ChaptersActivity.this, ReadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            setResult(SELECT_CHAPTER_CODE, intent);

            startActivity(intent);
            ChaptersActivity.this.finish();
        });

        btnSort = findViewById(R.id.ac_btnSort);
        btnSort.setOnClickListener(this);
        View btnUpdate = findViewById(R.id.ac_btnUpdate);
        btnUpdate.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_btnSort:
                if (btnSort.getText().equals("逆序")) {
                    listView.setSelection(chapterList.size() - 1);
                    btnSort.setText("正序");
                } else {
                    listView.setSelection(0);
                    btnSort.setText("逆序");
                }
                break;
            case R.id.ac_btnUpdate:
                ToastUtils.show("正在刷新目录...");
                getChapters(book, chapterList, false, handler, getMessage(1));
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChaptersActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}