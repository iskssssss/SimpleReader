package com.xrzx.reader.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.commonlibrary.utils.AndroidUtils;
import com.xrzx.commonlibrary.utils.ThemeUtils;
import com.xrzx.reader.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/7 17:58
 */
public class ReadContentSettingDialog {
    private Map<Integer, ReadPageStyle> readPageStyleMap = new HashMap<>();
    private TextView tvFontSizeText;
    private TextView tvFontTypeface;

    public void setFontSizeText(int fontSizeText) {
        tvFontSizeText.setText(String.valueOf(fontSizeText));
    }

    private TextView tvFontSizeLess;
    private TextView tvFontSizeIncrease;
    private SeekBar sbLuminance;

    public void setSbLuminance(int val) {
        this.sbLuminance.setProgress(val);
    }

    private CheckBox cbLuminanceSystem;

    public void setLuminanceSystem(boolean checked) {
        this.cbLuminanceSystem.setChecked(checked);
    }

    private ImageView llMoreItemImg;
    private ImageView llHorizontalScreenItemImg;
    private TextView tvHorizontalScreen;

    public ReadPageStyle getReadPageStyle(int key) {
        return readPageStyleMap.get(key);
    }

    private Dialog dialog = null;
    private View view;

    public View getView() {
        return view;
    }

    private Window window;

    public Window getWindow() {
        return window;
    }

    public ReadContentSettingDialog(Context context,
                                    View.OnClickListener onClickListener,
                                    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener,
                                    CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        initDialog(context, onClickListener, onSeekBarChangeListener, onCheckedChangeListener);
    }

    public void setReadPageSetting(ReadPageStyle readPageStyle) {
        window.setStatusBarColor(ContextCompat.getColor(dialog.getContext(), readPageStyle.getColorReadSetting()));
        view.findViewById(R.id.drcs_ll_main).setBackgroundResource(readPageStyle.getColorReadSetting());
    }


    public void initDialog(Context context,
                           View.OnClickListener onClickListener,
                           SeekBar.OnSeekBarChangeListener onSeekBarChangeListener,
                           CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        dialog = new Dialog(context, R.style.default_dialog_style);
        setView(dialog.getContext(), onClickListener, onSeekBarChangeListener, onCheckedChangeListener);
        dialog.setContentView(view);
        window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }
    HashMap<Integer, Integer> paramMap = new HashMap<>(4);

    @SuppressLint("ClickableViewAccessibility")
    private void setView(Context context,
                         View.OnClickListener onClickListener,
                         SeekBar.OnSeekBarChangeListener onSeekBarChangeListener,
                         CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_read_content_setting, null);
        view.findViewById(R.id.drcs_ll_main).setOnClickListener(null);

        // 阅读主题
        view.findViewById(R.id.drcs_read_btn_k61).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_k62).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_k63).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_k64).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_k65).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_k66).setOnClickListener(onClickListener);
        view.findViewById(R.id.drcs_read_btn_at_night).setOnClickListener(onClickListener);

        // 字体调整
        tvFontTypeface = view.findViewById(R.id.drcs_tv_font_typeface);
        tvFontTypeface.setOnClickListener(onClickListener);
        tvFontSizeText = view.findViewById(R.id.drcs_tv_font_size_text);
        tvFontSizeLess = view.findViewById(R.id.drcs_tv_font_size_less);
        tvFontSizeLess.setOnClickListener(onClickListener);
        tvFontSizeIncrease = view.findViewById(R.id.drcs_tv_font_size_increase);
        tvFontSizeIncrease.setOnClickListener(onClickListener);

        // 亮度调整
        sbLuminance = view.findViewById(R.id.drcs_sb_luminance);
        sbLuminance.setOnSeekBarChangeListener(onSeekBarChangeListener);

        // 跟随系统亮度
        cbLuminanceSystem = view.findViewById(R.id.drcs_cb_luminance_system);
        cbLuminanceSystem.setOnCheckedChangeListener(onCheckedChangeListener);

        // 横屏切换
        LinearLayout llHorizontalScreenItem = view.findViewById(R.id.drcs_ll_horizontal_screen_item);
        llHorizontalScreenItem.setOnClickListener(onClickListener);
        llHorizontalScreenItemImg = view.findViewById(R.id.drcs_ll_horizontal_screen_item_img);
        tvHorizontalScreen = view.findViewById(R.id.drcs_tv_horizontal_screen);

        // 更多
        LinearLayout llMoreItem = view.findViewById(R.id.drcs_ll_more_item);
        llMoreItem.setOnClickListener(onClickListener);
        llMoreItemImg =  view.findViewById(R.id.drcs_ll_more_item_img);

        readPageStyleMap.put(R.id.drcs_read_btn_k61, ReadPageStyle.READ_YD_K61);
        readPageStyleMap.put(R.id.drcs_read_btn_k62, ReadPageStyle.READ_YD_K62);
        readPageStyleMap.put(R.id.drcs_read_btn_k63, ReadPageStyle.READ_YD_K63);
        readPageStyleMap.put(R.id.drcs_read_btn_k64, ReadPageStyle.READ_YD_K64);
        readPageStyleMap.put(R.id.drcs_read_btn_k65, ReadPageStyle.READ_YD_K65);
        readPageStyleMap.put(R.id.drcs_read_btn_k66, ReadPageStyle.READ_YD_K66);
        readPageStyleMap.put(R.id.drcs_read_btn_at_night, ReadPageStyle.READ_YD_AtNight);
        view.setOnTouchListener((v, e) -> {
            dialog.dismiss();
            return false;
        });

        paramMap.put(R.id.drcs_ll_more_item_img, R.attr.MoreBackground);
    }

    public void changeTheme(Resources.Theme theme) {
        ThemeUtils.applyTextColor(cbLuminanceSystem, theme, R.attr.TextViewColor);
        ThemeUtils.changeTheme(view.findViewById(R.id.drcs_ll_main), theme);
        ThemeUtils.changeImageViewTheme(view.findViewById(R.id.drcs_ll_main), theme, paramMap);
    }

    public void show() {
        dialog.show();
    }


    public void dismiss() {
        dialog.dismiss();
    }

    public void setHorizontalScreenIco(boolean horizontalScreen,boolean atNight) {
        ViewGroup.LayoutParams params = llHorizontalScreenItemImg.getLayoutParams();
        if (horizontalScreen) {
            tvHorizontalScreen.setText("竖屏模式");
            params.width = AndroidUtils.dip2px(dialog.getContext(),16f);
            params.height = AndroidUtils.dip2px(dialog.getContext(),24f);
            llHorizontalScreenItemImg.setMaxWidth(16);
            llHorizontalScreenItemImg.setMaxHeight(24);
        } else {
            tvHorizontalScreen.setText("横屏模式");
            params.width = AndroidUtils.dip2px(dialog.getContext(),24f);
            params.height = AndroidUtils.dip2px(dialog.getContext(),16f);
        }
        llHorizontalScreenItemImg.setLayoutParams(params);
        if (atNight) {
            if (horizontalScreen) {
                llHorizontalScreenItemImg.setBackgroundResource(R.drawable.ico_vertical_screen_night);
            } else {
                llHorizontalScreenItemImg.setBackgroundResource(R.drawable.ico_horizontal_screen_night);
            }
            return;
        }
        if (horizontalScreen) {
            llHorizontalScreenItemImg.setBackgroundResource(R.drawable.ico_vertical_screen);
        } else {
            llHorizontalScreenItemImg.setBackgroundResource(R.drawable.ico_horizontal_screen);
        }
    }

    public void setFontColor(int color, boolean atNight) {
        window.getDecorView().setSystemUiVisibility(atNight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void finish() {
        dismiss();
        view.setOnTouchListener(null);
        view.findViewById(R.id.drcs_read_btn_k61).setOnClickListener(null);
        view.findViewById(R.id.drcs_read_btn_k62).setOnClickListener(null);
        view.findViewById(R.id.drcs_read_btn_k63).setOnClickListener(null);
        view.findViewById(R.id.drcs_read_btn_k64).setOnClickListener(null);
        view.findViewById(R.id.drcs_read_btn_k65).setOnClickListener(null);
        view.findViewById(R.id.drcs_read_btn_k66).setOnClickListener(null);
        tvFontTypeface.setOnClickListener(null);
        tvFontSizeLess.setOnClickListener(null);
        tvFontSizeIncrease.setOnClickListener(null);
        sbLuminance.setOnSeekBarChangeListener(null);
        cbLuminanceSystem.setOnCheckedChangeListener(null);
        readPageStyleMap.clear();
        readPageStyleMap = null;
        tvFontSizeText = null;
        tvFontTypeface = null;
        tvFontSizeLess = null;
        tvFontSizeIncrease = null;
        sbLuminance = null;
        cbLuminanceSystem = null;
        dialog = null;
        window = null;
        view = null;
    }
}
