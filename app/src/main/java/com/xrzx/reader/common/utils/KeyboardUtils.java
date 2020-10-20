package com.xrzx.reader.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
}
