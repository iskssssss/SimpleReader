package com.xrzx.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.book.entity.ChapterInfo;

public class BookInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Book book;
    private Button btnAddBookshelf;
    private Button btnReadNow;
    private TextView tVName;
    private TextView tVAuthor;
    private TextView tVType;
    private TextView tVLastUpdateTime;
    private TextView tVIntroduction;
    private TextView tVLastUpdateChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        btnAddBookshelf = findViewById(R.id.abi_btnAddBookshelf);
        btnAddBookshelf.setOnClickListener(this);
        btnReadNow = findViewById(R.id.abi_btnReadNow);
        btnReadNow.setOnClickListener(this);

        tVName = findViewById(R.id.abi_tVName);
        tVAuthor = findViewById(R.id.abi_tVAuthor);
        tVType = findViewById(R.id.abi_tVType);
        tVLastUpdateTime = findViewById(R.id.abi_tVLastUpdateTime);
        tVIntroduction = findViewById(R.id.abi_tVIntroduction);
        tVLastUpdateChapter = findViewById(R.id.abi_tVLastUpdateChapter);

        book = (Book) getIntent().getSerializableExtra("book");
        tVName.setText(book.getName());
        tVAuthor.setText(book.getAuthor());
        tVType.setText(book.getType());
        tVLastUpdateTime.setText(book.getLastUpdateTime());
        tVIntroduction.setText(book.getIntroduction());
        tVLastUpdateChapter.setText(book.getLastUpdateChapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abi_btnAddBookshelf:
                onClick_btnAddBookshelf(v);
                break;
            case R.id.abi_btnReadNow:
                onClick_btnReadNow(v);
                break;
            default:
                //TODO ...
                break;
        }
    }

    /**
     * 立即阅读
     *
     * @param v View
     */
    private void onClick_btnReadNow(View v) {
        Intent intent = new Intent(BookInfoActivity.this, ChaptersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    /**
     * 加入书架
     *
     * @param v View
     */
    private void onClick_btnAddBookshelf(View v) {
    }
}