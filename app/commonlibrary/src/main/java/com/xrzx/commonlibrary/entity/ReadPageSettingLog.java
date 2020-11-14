package com.xrzx.commonlibrary.entity;

import com.xrzx.commonlibrary.database.dao.ReadPageSettingDao;
import com.xrzx.commonlibrary.enums.ReadPageStyle;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/7 23:52
 */
public class ReadPageSettingLog {
    /**
     * 字体
     */
    private int typeface;

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 亮度
     */
    private int luminance;

    /**
     * 系统亮度
     */
    private int luminanceSystem;

    /**
     * 阅读风格
     */
    private ReadPageStyle readPageStyle;

    /**
     * 夜间模式
     */
    private int atNight;

    /**
     * 是否时横屏
     */
    private int horizontalScreen;


    /**
     * 行间距
     */
    private int rowSpacing;

    /**
     * 页边距
     */
    private int pageSpacing;

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getLuminance() {
        return luminance;
    }

    public void setLuminance(int luminance) {
        this.luminance = luminance;
    }

    public int getLuminanceSystem() {
        return luminanceSystem;
    }

    public void setLuminanceSystem(int luminanceSystem) {
        this.luminanceSystem = luminanceSystem;
    }

    public ReadPageStyle getReadPageStyle() {
        return readPageStyle;
    }

    public void setReadPageStyle(ReadPageStyle readPageStyle) {
        this.readPageStyle = readPageStyle;
    }

    public int getAtNight() {
        return atNight;
    }

    public void setAtNight(int atNight) {
        this.atNight = atNight;
    }

    public int getHorizontalScreen() {
        return horizontalScreen;
    }

    public void setHorizontalScreen(int horizontalScreen) {
        this.horizontalScreen = horizontalScreen;
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

    public void setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
    }

    public int getPageSpacing() {
        return pageSpacing;
    }

    public void setPageSpacing(int pageSpacing) {
        this.pageSpacing = pageSpacing;
    }

    public void updateTypeface(int typeface) {
        this.typeface = typeface;
        ReadPageSettingDao.update("typeface", String.valueOf(this.typeface));
    }


    public int increaseFontSize() {
        if (this.fontSize >= 56) {
            return this.fontSize;
        }
        ++this.fontSize;
        ReadPageSettingDao.update("font_size", String.valueOf(this.fontSize));
        return this.fontSize;
    }

    public int lessFontSize() {
        if (this.fontSize <= 12) {
            return this.fontSize;
        }
        --this.fontSize;
        ReadPageSettingDao.update("font_size", String.valueOf(this.fontSize));
        return this.fontSize;
    }

    public boolean gLuminanceSystem() {
        return luminanceSystem == 1;
    }

    public void sLuminanceSystem(boolean luminanceSystem) {
        this.luminanceSystem = luminanceSystem ? 1 : 0;
    }

    public boolean gAtNight() {
        return atNight == 1;
    }

    public void updateReadPageStyle(ReadPageStyle readPageStyle) {
        this.readPageStyle = readPageStyle;
        ReadPageSettingDao.update("read_page_style", this.readPageStyle.name());
    }

    public void updateAtNight(boolean atNight) {
        this.atNight = atNight ? 1 : 0;
        ReadPageSettingDao.update("at_night", String.valueOf(this.atNight));
    }


    public boolean gHorizontalScreen() {
        return this.horizontalScreen == 1;
    }

    public void updateHorizontalScreen(boolean horizontalScreen) {
        this.horizontalScreen = horizontalScreen ? 1 : 0;
        ReadPageSettingDao.update("horizontal_screen", String.valueOf(this.horizontalScreen));
    }
}
