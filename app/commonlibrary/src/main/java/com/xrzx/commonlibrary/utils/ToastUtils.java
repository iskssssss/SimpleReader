package com.xrzx.commonlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

/**
 * @Description
 * @Author ks
 * @Date 2020/10/30 23:20
 */
public class ToastUtils {
    private static Context context;
    private static Toast toast = null;

    @SuppressLint("ShowToast")
    public static void setToast(Context c) {
        if (toast == null) {
            context = c;
            toast = Toast.makeText(c, "", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 显示提示信息
     *
     * @param text 提示内容
     */
    public static void show(CharSequence text) {
        try {
            toast.setText(text);
            toast.show();
        } catch (Exception e) {
            try {
                toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                toast.setText(text);
                toast.show();
            } catch (Exception e1) {
                Log.e("ToastUtils", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }
    }
}
