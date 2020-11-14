package com.xrzx.commonlibrary.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xrzx.commonlibrary.R;
import com.xrzx.commonlibrary.ThemeUIInterface;
import com.xrzx.commonlibrary.view.custom.ThemeImageView;
import com.xrzx.commonlibrary.view.custom.ThemeTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/11 22:45
 */
public class ThemeUtils {

    /**
     * 递归改变控件主题
     *
     * @param rootView 主视图
     * @param theme    主题
     */
    public static void changeTheme(View rootView, Resources.Theme theme) {
        if (rootView instanceof ThemeUIInterface) {
            ((ThemeUIInterface) rootView).setTheme(theme);
        }
        if (rootView instanceof ViewGroup) {
            int count = ((ViewGroup) rootView).getChildCount();
            for (int i = 0; i < count; i++) {
                changeTheme(((ViewGroup) rootView).getChildAt(i), theme);
            }
        }
    }

    /**
     * 递归改变控件主题
     *
     * @param rootView 主视图
     * @param theme    主题
     */
    public static void changeTextViewTheme(View rootView, Resources.Theme theme) {
        if (rootView instanceof ThemeTextView) {
            ((ThemeUIInterface) rootView).setTheme(theme);
        }
        if (rootView instanceof ViewGroup) {
            int count = ((ViewGroup) rootView).getChildCount();
            for (int i = 0; i < count; i++) {
                changeTextViewTheme(((ViewGroup) rootView).getChildAt(i), theme);
            }
        }
    }

    public static void changeImageViewTheme(View rootView, Resources.Theme theme, HashMap<Integer, Integer> paramMap) {
        if (null == rootView || null == paramMap || paramMap.isEmpty()) {
            return;
        }
        if (rootView instanceof ThemeImageView) {
            final Integer integer = paramMap.get(rootView.getId());
            if (null != integer) {
                ((ThemeUIInterface) rootView).setTheme(theme, integer);
            }
        }
        if (rootView instanceof ViewGroup) {
            int count = ((ViewGroup) rootView).getChildCount();
            for (int i = 0; i < count; i++) {
                changeImageViewTheme(((ViewGroup) rootView).getChildAt(i), theme, paramMap);
            }
        }
    }


    public static int getTextColorAttribute(AttributeSet attr) {
        return getAttributeValue(attr, R.attr.TextViewColor);
    }

    public static int getAttributeValue(AttributeSet attr, int paramInt) {
        int value = -1;
        int count = attr.getAttributeCount();
        for (int i = 0; i < count; i++) {
            if (attr.getAttributeNameResource(i) == paramInt) {
                String str = attr.getAttributeValue(i);
                if (null != str && str.startsWith("?")) {
                    value = Integer.parseInt(str);
                    return value;
                }
            }
        }
        return value;
    }

    public static void applyTextColor(View ci, Resources.Theme theme, int paramInt) {
        TypedArray ta = theme.obtainStyledAttributes(new int[]{paramInt});
        int resourceId = ta.getColor(0, 0);
        if (ci instanceof EditText) {
            ((TextView) ci).setTextColor(resourceId);
            ((TextView) ci).setHintTextColor(resourceId);
            ((TextView) ci).setLinkTextColor(resourceId);
        } else if (ci instanceof TextView) {
            ((TextView) ci).setTextColor(resourceId);
        }
        ta.recycle();
    }

    public static void applyBackground(View ci, Resources.Theme theme, int paramInt) {
        TypedArray ta = theme.obtainStyledAttributes(new int[]{paramInt});
        Drawable resourceId = ta.getDrawable(0);
//        if (ci instanceof ThemeImageView) {
        ci.setBackground(resourceId);
//        }
        ta.recycle();
    }
}
