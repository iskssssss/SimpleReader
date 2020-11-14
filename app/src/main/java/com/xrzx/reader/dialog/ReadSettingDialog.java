package com.xrzx.reader.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.ThemeUtils;
import com.xrzx.reader.R;
import com.xrzx.commonlibrary.view.custom.ThemeTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/7 14:36
 */
public class ReadSettingDialog {
    private Window window;
    private Dialog dialog = null;
    private View view;

    public View getView() {
        return view;
    }

    private ThemeTextView tvChapterName;
    private LinearLayout llBack;
    private LinearLayout llMore;
    private TextView tvPrevChapter;
    private TextView tvNextChapter;
    private LinearLayout llCatalogItem;
    private LinearLayout llDownloadItem;
    private LinearLayout llNightItem;
    private LinearLayout llSettingItem;
    private TextView tvNightItemText;


    public void setChapterName(String title) {
        this.tvChapterName.setText(title);
    }

    public ReadSettingDialog(Context context, View.OnClickListener onClickListener) {
        initDialog(context, onClickListener);
    }

    public void setReadPageSetting(ReadPageStyle readPageStyle) {
        window.setStatusBarColor(ContextCompat.getColor(dialog.getContext(), readPageStyle.getColorReadSetting()));
        view.findViewById(R.id.drs_ll_top_menu).setBackgroundResource(readPageStyle.getColorReadSetting());
        view.findViewById(R.id.drs_ll_button_menu).setBackgroundResource(readPageStyle.getColorReadSetting());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDialog(Context context, View.OnClickListener onClickListener) {
        dialog = new Dialog(context, R.style.default_dialog_style);
        setView(context, onClickListener);
        dialog.setContentView(view);
        window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        view.setOnTouchListener((v, e) -> {
            dialog.dismiss();
            return false;
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    HashMap<Integer, Integer> paramMap = new HashMap<>(4);

    @SuppressLint("ClickableViewAccessibility")
    private void setView(Context context, View.OnClickListener onClickListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_read_setting, null);
        view.findViewById(R.id.drs_ll_top_menu).setOnClickListener(null);
        view.findViewById(R.id.drs_ll_button_menu).setOnClickListener(null);

        // 返回
        llBack = view.findViewById(R.id.drs_ll_back);
        llBack.setOnClickListener(onClickListener);
        // 设置标题
        tvChapterName = view.findViewById(R.id.drs_tv_chapter_name);
        // 更多
        llMore = view.findViewById(R.id.drs_ll_more);
        llMore.setOnClickListener(onClickListener);
        // 上一章
        tvPrevChapter = view.findViewById(R.id.drs_tv_prev_chapter);
        tvPrevChapter.setOnClickListener(onClickListener);
        // 下一章
        tvNextChapter = view.findViewById(R.id.drs_tv_next_chapter);
        tvNextChapter.setOnClickListener(onClickListener);
        // 目录
        llCatalogItem = view.findViewById(R.id.drs_ll_catalog_item);
        llCatalogItem.setOnClickListener(onClickListener);
        // 缓存
        llDownloadItem = view.findViewById(R.id.drs_ll_download_item);
        llDownloadItem.setOnClickListener(onClickListener);
        // 夜间
        llNightItem = view.findViewById(R.id.drs_ll_night_item);
        tvNightItemText = view.findViewById(R.id.drs_tv_3);
        llNightItem.setOnClickListener(onClickListener);
        // 设置
        llSettingItem = view.findViewById(R.id.drs_ll_setting_item);
        llSettingItem.setOnClickListener(onClickListener);


        paramMap.put(R.id.drs_ll_back_item_img, R.attr.BackBackground);
        paramMap.put(R.id.drs_ll_more_item_img, R.attr.MoreBackground);
        paramMap.put(R.id.drs_ll_catalog_item_img, R.attr.DialogCatalogBackground);
        paramMap.put(R.id.drs_ll_download_item_img, R.attr.DialogDownloadBackground);
        paramMap.put(R.id.drs_ll_night_item_img, R.attr.DialogNightBackground);
        paramMap.put(R.id.drs_ll_setting_item_img, R.attr.DialogSettingBackground);
    }

    public void changeTheme(Resources.Theme theme) {
        ThemeUtils.changeTheme(view.findViewById(R.id.drs_ll_button_menu), theme);
        ThemeUtils.changeTheme(view.findViewById(R.id.drs_ll_top_menu), theme);


        ThemeUtils.changeImageViewTheme(view.findViewById(R.id.drs_ll_button_menu), theme, paramMap);
        ThemeUtils.changeImageViewTheme(view.findViewById(R.id.drs_ll_top_menu), theme, paramMap);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void finish() {
        dismiss();
        view.setOnTouchListener(null);
        llBack.setOnClickListener(null);
        llMore.setOnClickListener(null);
        tvPrevChapter.setOnClickListener(null);
        tvNextChapter.setOnClickListener(null);
        llCatalogItem.setOnClickListener(null);
        llDownloadItem.setOnClickListener(null);
        llNightItem.setOnClickListener(null);
        llSettingItem.setOnClickListener(null);

        llBack = null;
        llMore = null;
        tvPrevChapter = null;
        tvNextChapter = null;
        llCatalogItem = null;
        llDownloadItem = null;
        llNightItem = null;
        llSettingItem = null;
        tvChapterName = null;

        window = null;
        dialog = null;
        view = null;
    }

    public void setNightItemText(boolean atNight) {
        tvNightItemText.setText(atNight ? "日间" : "夜间");
    }

    public void setFontColor(int color, boolean atNight) {
        setNightItemText(atNight);
        window.getDecorView().setSystemUiVisibility(atNight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}
