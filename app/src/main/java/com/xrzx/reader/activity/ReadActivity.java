package com.xrzx.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xrzx.reader.view.customize.AutoMarqueeTextView;
import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.ChapterInfo;

public class ReadActivity extends AppCompatActivity {
    private ChapterInfo chapterInfo;
    private TextView tVTitle;
    private AutoMarqueeTextView tVContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏

        setContentView(R.layout.activity_read);

        tVTitle = findViewById(R.id.tV_title);
        tVContent = findViewById(R.id.tV_content);

        chapterInfo = (ChapterInfo) getIntent().getSerializableExtra("chapterInfo");
        tVTitle.setText(chapterInfo.getcTitle());
        tVContent.setText(chapterInfo.getcContent());
    }
}