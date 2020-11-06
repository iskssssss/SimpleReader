package com.xrzx.reader;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/2 20:25
 */
public class LoadDialogUtils {
    private static Dialog createLoadingDialog(Context context,DialogInterface.OnKeyListener onKeyListener, String messageOne, String messageTwo) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_loading, null);
        LinearLayout linearLayout = (LinearLayout) view;
        final TextView tvMessageOne = view.findViewById(R.id.dl_tv_message_one);
        tvMessageOne.setText(messageOne);
        final TextView tvMessageTwo = view.findViewById(R.id.dl_tv_message_two);
        tvMessageTwo.setText(messageTwo);

        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setOnKeyListener(onKeyListener);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("linearLayout --- public void onClick(View v)");
            }
        });
        // 是否可以按“返回键”消失
        dialog.setCancelable(true);
        // 点击加载框以外的区域
        dialog.setCanceledOnTouchOutside(false);
        // 设置布局
        dialog.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        dialog.show();
        return dialog;
    }

    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
}
