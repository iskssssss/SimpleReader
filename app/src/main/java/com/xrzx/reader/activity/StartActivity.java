package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


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

public class StartActivity extends AppCompatActivity {

    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("加载中...");
                    Thread.sleep(3000);
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    StartActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}