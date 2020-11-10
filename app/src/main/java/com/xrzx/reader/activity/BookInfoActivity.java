package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrzx.commonlibrary.utils.DateUtils;
import com.xrzx.reader.R;
import com.xrzx.reader.activity.base.BaseActivity;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.database.dao.BookInfoDao;
import com.xrzx.commonlibrary.utils.ToastUtils;

/**
 * @Description 书籍信息页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookInfoActivity extends BaseActivity implements View.OnClickListener {

    private Button btnAddBookshelf;

    private TextView tvName;
    private TextView tvAuthor;
    private TextView tvType;
    private TextView tvLastUpdateTime;
    private TextView tvIntroduction;
    private TextView tvLastUpdateChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        initView();
        setCurrentBook();
        if (currentBook.isBookShelf()) {
            btnAddBookshelf.setText(getString(R.string.remove_bookshelf_str));
        }
        tvName.setText(currentBook.getbName());
        tvAuthor.setText(currentBook.getbAuthor());
        tvType.setText(currentBook.getbType());
        tvLastUpdateTime.setText(currentBook.getbLastUpdateTime());
        tvIntroduction.setText(currentBook.getbIntroduction());
        tvLastUpdateChapter.setText(currentBook.getbLastUpdateChapter());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        findViewById(R.id.abi_ll_back).setOnClickListener(this);
        findViewById(R.id.abi_ll_more).setOnClickListener(this);
        findViewById(R.id.abi_btn_read_now).setOnClickListener(this);
        btnAddBookshelf = findViewById(R.id.abi_btn_add_book_shelf);
        btnAddBookshelf.setOnClickListener(this);

        LinearLayout llChapterItem = findViewById(R.id.abi_ll_chapterItem);
        llChapterItem.setOnClickListener(this);

        ImageView ivCover = findViewById(R.id.abi_iv_cover);
        ivCover.setBackgroundResource(R.drawable.cover);
        tvName = findViewById(R.id.abi_tv_name);
        tvAuthor = findViewById(R.id.abi_tv_author);
        tvType = findViewById(R.id.abi_tv_type);
        tvLastUpdateTime = findViewById(R.id.abi_tv_last_update_time);
        tvIntroduction = findViewById(R.id.abi_tv_introduction);
        tvLastUpdateChapter = findViewById(R.id.abi_tv_last_update_chapter_title);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abi_btn_add_book_shelf:
                onClickBtnAddBookShelf(v);
                break;
            case R.id.abi_btn_read_now:
                onClickBtnReadNow(v);
                break;
            case R.id.abi_ll_chapterItem:
                onClickLlChapterItem(v);
                break;
            case R.id.abi_ll_back:
                BookInfoActivity.this.finish();
                break;
            case R.id.abi_ll_more:
                ToastUtils.show("更多");
                break;
            default:
                // TODO...
                break;
        }
    }

    /**
     * 目录
     *
     * @param v View
     */
    private void onClickLlChapterItem(View v) {
        Intent intent = new Intent(BookInfoActivity.this, ChaptersActivity.class);
        startActivity(intent);
    }

    /**
     * 立即阅读
     *
     * @param v View
     */
    private void onClickBtnReadNow(View v) {
        // 更新书籍阅读时间
        currentBook.setbNewReadTime(DateUtils.getDateTime());
        if (currentBook.isBookShelf()) {
            GLOBAL_DATA.putBookFirst();
            BookInfoDao.updateBook(new Book(currentBook.getbUniquelyIdentifies(), currentBook.getbNewReadTime()));
        }

        Intent intent = new Intent(BookInfoActivity.this, ReadActivity.class);
        startActivity(intent);
    }

    /**
     * 加入书架
     *
     * @param v View
     */
    private void onClickBtnAddBookShelf(View v) {
        if (getString(R.string.add_bookshelf_str).contentEquals(btnAddBookshelf.getText())) {
            GLOBAL_DATA.putBook(currentBook);
            setBtnAddBookShelfStyle("加入成功.", getString(R.string.remove_bookshelf_str));
        } else {
            GLOBAL_DATA.removeBook(currentBook);
            setBtnAddBookShelfStyle("移除成功.", getString(R.string.add_bookshelf_str));
        }
    }

    private void setBtnAddBookShelfStyle(String showText, String text) {
        ToastUtils.show(showText);
        btnAddBookshelf.setText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            GLOBAL_DATA.removeSelectBook();
            BookInfoActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}