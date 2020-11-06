package com.xrzx.commonlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/30 23:20
 */
public class ToastUtils {
    private static Toast toast = null;

    @SuppressLint("ShowToast")
    public static void setToast(Context context) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 显示提示信息
     *
     * @param text 提示内容
     */
    public static void show(CharSequence text) {
        toast.setText(text);
        toast.show();
    }
}
