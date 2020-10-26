package com.xrzx.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Chapter;

/**
 * @Description 阅读页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ReadActivity extends AppCompatActivity {
    private Chapter chapter;
    private TextView tVTitle;
    private TextView tVContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_read);

        tVTitle = findViewById(R.id.tV_title);
        tVContent = findViewById(R.id.tV_content);

        chapter = (Chapter) getIntent().getSerializableExtra("chapterInfo");
        tVTitle.setText(chapter.getcTitle());
        tVContent.setText(chapter.getcContent());
    }
}