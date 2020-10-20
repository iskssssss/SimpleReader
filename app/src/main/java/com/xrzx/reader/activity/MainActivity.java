package com.xrzx.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.book.http.BookHttpApi;
import com.xrzx.reader.common.utils.KeyboardUtils;
import com.xrzx.reader.common.http.callback.ResultCallBack;
import com.xrzx.reader.view.adapter.SearchBookAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private EditText eTSearchByBookName;
    private Button btnSearch;

    private ListView listView;
    private ArrayList<Book> titleList = new ArrayList<>();
    private SearchBookAdapter chapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTSearchByBookName = findViewById(R.id.eTSearchByBookName);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        listView = findViewById(R.id.listView);
        chapterAdapter = new SearchBookAdapter(MainActivity.this, R.layout.search_book_item, titleList);
        listView.setAdapter(chapterAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> executorService.execute(() -> {
            Book book = titleList.get(position);
            BookHttpApi.getBookDetailsInfo(book, new ResultCallBack<Book>() {
                @Override
                public void onSuccess(Book result) {
                    Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("book", book);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                onClick_btnSearch(v);
                break;
            default:
                //TODO ...
                break;
        }
    }

    private void onClick_btnSearch(View v) {
        String searchkey = eTSearchByBookName.getText().toString();
        if ("".equals(searchkey)) {
            return;
        }
        titleList.clear();
        FormBody.Builder searchDict = new FormBody.Builder();
        searchDict.add("searchkey", searchkey);
        BookHttpApi.searchBooks(searchDict, titleList, new ResultCallBack<List<Book>>() {
            @Override
            public void onSuccess(List<Book> result) {
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        KeyboardUtils.hideKeyboard(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    chapterAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    System.out.println("ss");
                    break;
            }
        }
    };
}