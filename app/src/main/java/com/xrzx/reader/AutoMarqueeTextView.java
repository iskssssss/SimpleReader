package com.xrzx.reader;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class AutoMarqueeTextView extends AppCompatTextView {
    public AutoMarqueeTextView(@NonNull Context context) {
        super(context);
        setFocusable(true);
    }

    public AutoMarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    public AutoMarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        // TODO ......
    }
}
