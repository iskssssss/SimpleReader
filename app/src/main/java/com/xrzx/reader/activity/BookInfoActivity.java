package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    private Button btnReadNow;

    private ImageView iVCover;
    private TextView tVName;
    private TextView tVAuthor;
    private TextView tVType;
    private TextView tVLastUpdateTime;
    private TextView tVIntroduction;
    private TextView tVLastUpdateChapter;
    private LinearLayout lLChapterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        initView();
        getBookInfo();
        if (book.isBookShelf()) {
            book.setBookShelf(true);
            btnAddBookshelf.setText(getString(R.string.remove_bookshelf_str));
        }
        tVName.setText(book.getbName());
        tVAuthor.setText(book.getbAuthor());
        tVType.setText(book.getbType());
        tVLastUpdateTime.setText(book.getbLastUpdateTime());
        tVIntroduction.setText(book.getbIntroduction());
        tVLastUpdateChapter.setText(book.getbLastUpdateChapter());
    }

    /**
     * 初始化控件
     */
    private void initView() {

        btnAddBookshelf = findViewById(R.id.abi_btnAddBookshelf);
        btnAddBookshelf.setOnClickListener(this);
        btnReadNow = findViewById(R.id.abi_btnReadNow);
        btnReadNow.setOnClickListener(this);

        lLChapterItem = findViewById(R.id.abi_LLChapterItem);
        lLChapterItem.setOnClickListener(this);

        iVCover = findViewById(R.id.abi_iVCover);
        iVCover.setBackgroundResource(R.drawable.cover);
        tVName = findViewById(R.id.abi_tVName);
        tVAuthor = findViewById(R.id.abi_tVAuthor);
        tVType = findViewById(R.id.abi_tVType);
        tVLastUpdateTime = findViewById(R.id.abi_tVLastUpdateTime);
        tVIntroduction = findViewById(R.id.abi_tVIntroduction);
        tVLastUpdateChapter = findViewById(R.id.abi_tVLastUpdateChapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abi_btnAddBookshelf:
                onClickBtnAddBookShelf(v);
                break;
            case R.id.abi_btnReadNow:
                onClickBtnReadNow(v);
                break;
            case R.id.abi_LLChapterItem:
                onClickLlChapterItem(v);
                break;
            default:
                //TODO ...
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
        book.setbNewReadTime(DateUtils.getDateTime());
        if (book.isBookShelf()) {
            GLOBAL_DATA.putBookFirst();
            BookInfoDao.updateBook(new Book(book.getbUniquelyIdentifies(), book.getbNewReadTime()));
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
            GLOBAL_DATA.putBook(book);
            setBtnAddBookShelfStyle("加入成功.", getString(R.string.remove_bookshelf_str));
        } else {
            GLOBAL_DATA.removeBook(book);
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
            GLOBAL_DATA.removeSelectBook();
            BookInfoActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}