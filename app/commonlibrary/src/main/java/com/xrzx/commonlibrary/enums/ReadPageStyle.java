package com.xrzx.commonlibrary.enums;

import com.xrzx.commonlibrary.R;

import java.time.format.DateTimeFormatter;

/**
 * @Description 阅读界面
 * @Author ks
 * @Date 2020/11/7 17:30
 */
public enum  ReadPageStyle {
    /**
     * 默认
     */
    READ_YD_K61(R.color.colorReadK61, R.color.colorReadSettingK61),
    /**
     * 淡绿色
     */
    READ_YD_K62(R.color.colorReadK62, R.color.colorReadSettingK62),
    /**
     *
     */
    READ_YD_K63(R.color.colorReadK63, R.color.colorReadSettingK63),
    /**
     * 淡粉色
     */
    READ_YD_K64(R.color.colorReadK64, R.color.colorReadSettingK64),
    /**
     * 淡蓝色
     */
    READ_YD_K65(R.color.colorReadK65, R.color.colorReadSettingK65),
    /**
     *
     */
    READ_YD_K66(R.color.colorReadK66, R.color.colorReadSettingK66),
    /**
     * 夜间
     */
    READ_YD_AtNight(R.color.colorReadAtNight, R.color.colorReadSettingAtNight);

    private final Integer colorRead;
    private final Integer colorReadSetting;

    ReadPageStyle(Integer colorRead, Integer colorReadSetting) {
        this.colorRead = colorRead;
        this.colorReadSetting = colorReadSetting;

    }

    public Integer getColorRead() {
        return colorRead;
    }

    public Integer getColorReadSetting() {
        return colorReadSetting;
    }

    public static ReadPageStyle str2ReadPageStyle(String readPageStyle) {
        for (ReadPageStyle value : ReadPageStyle.values()) {
            if (readPageStyle.equals(value.name())) {
                return value;
            }
        }
        return ReadPageStyle.READ_YD_K61;
    }

    @Override
    public String toString() {
        return name();
    }
}
