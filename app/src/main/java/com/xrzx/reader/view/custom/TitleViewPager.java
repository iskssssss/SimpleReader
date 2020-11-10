package com.xrzx.reader.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @Description 标题视图容器
 * @Author ks
 * @Date 2020/11/6 16:57
 */
public class TitleViewPager extends ViewPager {
    private boolean scroll = false;
    private boolean smoothScroll = false;

    public TitleViewPager(@NonNull Context context) {
        super(context);
    }

    public TitleViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scroll && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scroll && super.onTouchEvent(ev);

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, smoothScroll);
    }
}
