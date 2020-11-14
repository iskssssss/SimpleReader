package com.xrzx.commonlibrary.view.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/9 0:21
 */
public class BaseRecyclerView<T extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<T> {

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void clear() {
        // TODO ....
    }


    public interface OnItemClickListener {
        /**
         * 点击事件
         *
         * @param position 点击项
         */
        void onClick(Object item, int position);
    }

    public interface OnItemDetailsClickListener {
        /**
         * 点击事件
         *
         * @param position 点击项
         */
        void onClick(int position);
    }
}
