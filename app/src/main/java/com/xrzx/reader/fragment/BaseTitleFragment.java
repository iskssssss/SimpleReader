package com.xrzx.reader.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xrzx.commonlibrary.utils.ThemeUtils;
import com.xrzx.commonlibrary.view.custom.ThemeTextView;
import com.xrzx.reader.R;
import com.xrzx.reader.fragment.base.BaseFragment;

import javax.annotation.Nullable;

/**
 * @Description 基础标题
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BaseTitleFragment extends BaseFragment {
    private LinearLayout llMain;
    private final String title;
    private ThemeTextView tvTitle;

    public BaseTitleFragment(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_title, container, false);
        llMain = view.findViewById(R.id.fbt_ll_main);
        tvTitle = view.findViewById(R.id.fbt_tv_title);
        tvTitle.setText(title);
        return view;
    }

    @Override
    public void changeTheme(Resources.Theme theme) {
        tvTitle.setTheme(theme);
        ThemeUtils.applyBackground(llMain, theme, R.attr.TitleBackgroundColor);

    }
}
