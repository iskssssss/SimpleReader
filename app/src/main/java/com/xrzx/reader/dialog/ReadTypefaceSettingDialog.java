package com.xrzx.reader.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.entity.TypefaceEntity;
import com.xrzx.commonlibrary.enums.ReadPageStyle;
import com.xrzx.reader.R;
import com.xrzx.reader.view.adapter.TypefaceAdapter;
import com.xrzx.reader.view.base.BaseRecyclerView;

import java.util.ArrayList;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/10 17:59
 */
public class ReadTypefaceSettingDialog {

    private Dialog dialog = null;
    private View view;
    private RecyclerView rvTypefaces;
    private TypefaceAdapter typefaceAdapter;

    public View getView() {
        return view;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    private Window window;

    public Window getWindow() {
        return window;
    }


    public ReadTypefaceSettingDialog(Context context, TypefaceAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        initDialog(context, onItemClickListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDialog(Context context, BaseRecyclerView.OnItemClickListener onItemClickListener) {
        dialog = new Dialog(context, R.style.default_dialog_style);
        setView(context, onItemClickListener);
        dialog.setContentView(view);
        window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setOnTouchListener((v, e) -> {
            dialog.dismiss();
            return false;
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setView(Context context, BaseRecyclerView.OnItemClickListener onItemClickListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_read_typeface_setting, null);
        view.findViewById(R.id.drts_ll_main).setOnClickListener(null);
        rvTypefaces = view.findViewById(R.id.drts_rv_typefaces);
        rvTypefaces.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        ArrayList<TypefaceEntity> typefaceEntities = new ArrayList<>();
        typefaceEntities.add(new TypefaceEntity(0, "系统字体", Typeface.NORMAL));
        typefaceEntities.add(new TypefaceEntity(1, "仓耳与墨", R.font.font_1));
        typefaceEntities.add(new TypefaceEntity(2, "萌神拼音体", R.font.font_2));
        typefaceEntities.add(new TypefaceEntity(3, "王强书法体", R.font.font_3));
        typefaceEntities.add(new TypefaceEntity(4, "文鼎PL简报宋", R.font.font_4));
        typefaceEntities.add(new TypefaceEntity(5, "文鼎PL简中楷", R.font.font_5));
        typefaceEntities.add(new TypefaceEntity(6, "IPix中文像素字体", R.font.font_6));
        typefaceEntities.add(new TypefaceEntity(7, "千图小兔体", R.font.font_7));
        typefaceEntities.add(new TypefaceEntity(8, "演示悠然小楷", R.font.font_8));
        typefaceAdapter = new TypefaceAdapter(view.getContext(), R.layout.item_typeface, typefaceEntities);
        typefaceAdapter.setOnItemClickListener(onItemClickListener);
        typefaceAdapter.setCurrTypefaces(typefaceEntities.get(0));
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTypefaces.setLayoutManager(manager);
        rvTypefaces.setAdapter(typefaceAdapter);

    }

    public void setTypefaceId(int id){
        typefaceAdapter.setCurrTypefacesId(id);
    }

    public TypefaceEntity getCurrTypefaces() {
       return typefaceAdapter.getCurrTypefaces();
    }

    public void setReadPageSetting(ReadPageStyle readPageStyle) {
        window.setStatusBarColor(ContextCompat.getColor(context, readPageStyle.getColorReadSetting()));
        view.findViewById(R.id.drts_ll_main).setBackgroundResource(readPageStyle.getColorReadSetting());
    }


    public void setFontColor(int color, boolean atNight) {
        window.getDecorView().setSystemUiVisibility(atNight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ((TextView) view.findViewById(R.id.drts_tv_1)).setTextColor(color);
        typefaceAdapter.setFontColor(color);
        notifyDataSetChanged();
    }

    public void setCurrTypefaces(TypefaceEntity curr) {
        typefaceAdapter.setCurrTypefaces(curr);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        typefaceAdapter.notifyDataSetChanged();
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
        rvTypefaces.setAdapter(null);
        typefaceAdapter.clear();
        typefaceAdapter = null;
        context = null;
        dialog = null;
        window = null;
        view = null;
    }
}
