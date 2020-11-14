package com.xrzx.commonlibrary.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xrzx.commonlibrary.R;
import com.xrzx.commonlibrary.ThemeUIInterface;
import com.xrzx.commonlibrary.utils.ThemeUtils;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/11 21:37
 */
public class ThemeTextView extends androidx.appcompat.widget.AppCompatTextView implements ThemeUIInterface {

    public ThemeTextView(Context context) {
        super(context);
    }

    public ThemeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme theme) {
        setTheme(theme, R.attr.TextViewColor);
    }

    @Override
    public void setTheme(Resources.Theme theme, int paramInt) {
        if (paramInt == -1) {
            return;
        }
        ThemeUtils.applyTextColor(this, theme, paramInt);
    }
}
