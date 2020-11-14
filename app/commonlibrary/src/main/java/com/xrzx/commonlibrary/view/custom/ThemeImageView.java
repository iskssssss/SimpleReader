package com.xrzx.commonlibrary.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xrzx.commonlibrary.ThemeUIInterface;
import com.xrzx.commonlibrary.utils.ThemeUtils;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/11 23:10
 */
public class ThemeImageView extends androidx.appcompat.widget.AppCompatImageView implements ThemeUIInterface {


    public ThemeImageView(Context context) {
        super(context);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme theme) {
        setTheme(theme, -1);
    }

    @Override
    public void setTheme(Resources.Theme theme, int paramInt) {
        if (paramInt == -1) {
            return;
        }
        ThemeUtils.applyBackground(this, theme, paramInt);
    }
}
