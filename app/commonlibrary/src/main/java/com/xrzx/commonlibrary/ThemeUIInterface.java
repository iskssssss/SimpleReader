package com.xrzx.commonlibrary;

import android.content.res.Resources;
import android.view.View;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/11 21:38
 */
public interface ThemeUIInterface {

    /**
     * 视图
     *
     * @return 视图
     */
    public View getView();

    /**
     * 设置主题
     *
     * @param theme 主题
     */
    public void setTheme(Resources.Theme theme);

    /**
     * 设置主题
     *
     * @param theme    主题
     * @param paramInt attr
     */
    public void setTheme(Resources.Theme theme, int paramInt);
}