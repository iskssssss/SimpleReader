package com.xrzx.reader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.xrzx.reader.R;
import com.xrzx.reader.common.utils.DateUtils;
import com.xrzx.reader.common.utils.ThreadUtils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description 启动页
 * @Author ks
 * @Date 2020/10/26 11:37
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class StartActivity extends AppCompatActivity implements View.OnClickListener {


    private final static ExecutorService OTHER_EXECUTOR_SERVICE_THREAD_POOL = ThreadUtils.getOtherExecutorServiceThreadPool();
    private final static AtomicBoolean next = new AtomicBoolean(false);
    private TextView asTVCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        asTVCountdown = findViewById(R.id.as_tVCountdown);
        asTVCountdown.setOnClickListener(this);
        LocalDateTime startTime = DateUtils.now();
        OTHER_EXECUTOR_SERVICE_THREAD_POOL.execute(() -> {
            long millis;
            do {
                if (next.get()) {
                    return;
                }
                millis = DateUtils.durationByMillis(startTime, DateUtils.now());
                long countdown = 3 - (millis / 1000);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = Long.toString(countdown);
                handler.sendMessage(msg);
            } while (millis < 3000L);
            nextMain();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.as_tVCountdown:
                next.set(true);
                nextMain();
                break;
            default:
                break;
        }
    }

    private void nextMain() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        StartActivity.this.finish();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    asTVCountdown.setText(msg.obj + " | 点击跳过");
                    break;
                case 2:
                    System.out.println("ss");
                    break;
                default:
                    System.out.println("default");
                    break;
            }
        }
    };
}