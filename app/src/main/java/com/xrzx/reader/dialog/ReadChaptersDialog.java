package com.xrzx.reader.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.core.content.ContextCompat;

import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.reader.R;
import com.xrzx.reader.view.adapter.ChapterAdapter;

import java.util.ArrayList;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/8 12:12
 */
public class ReadChaptersDialog {

    /**
     * 当前阅读书籍
     */
    private Book currentBook;

    /**
     * 当前阅读的书籍目录
     */
    private ArrayList<Chapter> currentChapterList;
    private ChapterAdapter chapterAdapter;
    private ListView lvBookShelf;
    private Context context;
    private Dialog dialog = null;
    private Window window;
    private View view;

    public ReadChaptersDialog(Context context, Book currentBook, ArrayList<Chapter> currentChapterList, AdapterView.OnItemClickListener onClickListener) {
        this.context = context;
        this.currentBook = currentBook;
        this.currentChapterList = currentChapterList;
        initDialog(context, onClickListener);
    }

    public void setFontColor(int color, boolean atNight) {
        window.getDecorView().setSystemUiVisibility(atNight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        chapterAdapter.setFontColor(color);
        notifyDataSetChanged();
    }

    public void setItemSelectChapterIndex(int itemSelectChapterIndex) {
        this.chapterAdapter.setItemSelectChapterIndex(itemSelectChapterIndex);
        notifyDataSetChanged();
    }

    public void positionIndex(int height) {
        final float itemChapterHeight = AndroidUtils.sp2px(context, 56f);
        final float screenHeight = (float) height;
        int visibleListNumber = (int) Math.ceil((screenHeight / itemChapterHeight) / 2f);
        if (currentBook.getbCurrentReadChapterId() > visibleListNumber) {
            lvBookShelf.setSelection(currentBook.getbCurrentReadChapterId() - visibleListNumber);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initDialog(Context context, AdapterView.OnItemClickListener onClickListener) {
        dialog = new Dialog(context, R.style.default_dialog_style);
        setView(context, onClickListener);
        dialog.setContentView(view);
        window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setOnTouchListener((v, e) -> {
            dialog.dismiss();
            return false;
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setView(Context context, AdapterView.OnItemClickListener onClickListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_read_chapters, null);
        view.findViewById(R.id.drc_main).setOnClickListener(null);
        view.setOnTouchListener((v, e) -> {
            dialog.dismiss();
            return false;
        });

        chapterAdapter = new ChapterAdapter(context, R.layout.item_chapter, currentChapterList);
        chapterAdapter.setItemSelectChapterIndex(currentBook.getbCurrentReadChapterId());

        lvBookShelf = view.findViewById(R.id.drc_lv_book_shelf);
        lvBookShelf.setOnItemClickListener(onClickListener);
        lvBookShelf.setAdapter(chapterAdapter);
    }

    public void setReadPageSetting(ReadPageStyle readPageStyle) {
        window.setStatusBarColor(ContextCompat.getColor(context, readPageStyle.getColorReadSetting()));
        view.findViewById(R.id.drc_main).setBackgroundResource(readPageStyle.getColorReadSetting());
    }

    public void notifyDataSetChanged() {
        chapterAdapter.notifyDataSetChanged();
    }


    public void show(int height) {
        positionIndex(height);
        dialog.show();
    }


    public void dismiss() {
        dialog.dismiss();
    }

    public void finish() {
        dismiss();
        view.setOnTouchListener(null);
        lvBookShelf.setOnItemClickListener(null);
        lvBookShelf.setAdapter(null);

        currentBook = null;
        currentChapterList = null;
        chapterAdapter = null;
        lvBookShelf = null;
        context = null;
        dialog = null;
        window = null;
        view = null;
    }
}





















