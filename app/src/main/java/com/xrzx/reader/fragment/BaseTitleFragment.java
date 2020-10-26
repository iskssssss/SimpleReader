package com.xrzx.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xrzx.reader.R;

import javax.annotation.Nullable;

/**
 * @Description 基础标题
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BaseTitleFragment extends Fragment {

    private TextView btTVTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_title, container, false);
        btTVTitle = view.findViewById(R.id.bt_tVTitle);
        return view;
    }

    public void setTitle(CharSequence title) {
        btTVTitle.setText(title);
    }

    public String getTitle() {
        return btTVTitle.getText().toString();
    }
}
