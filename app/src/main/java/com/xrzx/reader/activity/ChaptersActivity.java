package com.xrzx.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Book;
import com.xrzx.reader.book.entity.ChapterInfo;
import com.xrzx.reader.book.http.BookHttpApi;
import com.xrzx.reader.common.http.callback.ResultCallBack;
import com.xrzx.reader.view.adapter.ChapterAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChaptersActivity extends AppCompatActivity {

    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);
    Book book;
    private ListView listView;
    private ArrayList<ChapterInfo> titleList = new ArrayList<>();
    private ChapterAdapter chapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);


        listView = findViewById(R.id.listView);
        chapterAdapter = new ChapterAdapter(ChaptersActivity.this, R.layout.chapter_item, titleList);
        listView.setAdapter(chapterAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> executorService.execute(() -> {
            ChapterInfo chapterInfo = titleList.get(position);
            BookHttpApi.getBookChapterContent(chapterInfo, new ResultCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    chapterInfo.setcContent(result);
                    Intent intent = new Intent(ChaptersActivity.this, ReadActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("chapterInfo", chapterInfo);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }));


        book = (Book) getIntent().getSerializableExtra("book");
        executorService.execute(() -> BookHttpApi.getBookChapters(book, titleList, new ResultCallBack<List<ChapterInfo>>() {
            @Override
            public void onSuccess(List<ChapterInfo> result) {
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        }));
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