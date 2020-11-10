package com.xrzx.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xrzx.reader.R;
import com.xrzx.reader.fragment.base.BaseFragment;

import javax.annotation.Nullable;

/**
 * @Description 基础标题
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BaseTitleFragment extends BaseFragment {

    private final String title;

    public BaseTitleFragment(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_title, container, false);
//        view.getContext().setTheme(R.style.AppTitle);
        TextView tvTitle = view.findViewById(R.id.fbt_tv_title);
        tvTitle.setText(title);
        return view;
    }
}
