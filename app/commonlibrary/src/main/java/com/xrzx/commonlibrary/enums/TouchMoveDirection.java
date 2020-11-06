package com.xrzx.commonlibrary.enums;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/29 12:26
 */
public enum TouchMoveDirection {
    /**
     * 左
     */
    LEFT(0),
    /**
     * 中
     */
    MIDDLE(1),
    /**
     * 右
     */
    RIGHT(2);

    private final Integer format;

    TouchMoveDirection(Integer format) {
        this.format = format;
    }

    public Integer getFormat() {
        return format;
    }
}
