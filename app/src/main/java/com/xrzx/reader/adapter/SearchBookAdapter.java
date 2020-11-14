package com.xrzx.reader.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.reader.R;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.view.base.BaseRecyclerView;

import java.util.ArrayList;

/**
 * @Description 查询 适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class SearchBookAdapter extends BaseRecyclerView<SearchBookAdapter.ViewHolder> {

    private final int resourceId;
    private final Context context;
    private int fontColor;
    private final ArrayList<Book> books;

    public SearchBookAdapter(Context context, int resourceId, ArrayList<Book> books) {
        this.context = context;
        this.resourceId = resourceId;
        this.books = books;
        changeTheme(context.getTheme());
    }

    public void changeTheme(Resources.Theme theme) {
        TypedValue typedValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.TextViewColor, typedValue, false)) {
            this.fontColor = context.getColor(typedValue.data);
        } else {
            this.fontColor = context.getColor(R.color.colorReadFontColorBlack);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.llItemMain.setOnClickListener(v -> onItemClickListener.onClick(book, position));
        holder.ivCover.setBackgroundResource(R.drawable.cover);
        holder.tvName.setText(book.getbName());
        holder.tvName.setTextColor(fontColor);
        holder.tvAuthor.setText(book.getbAuthor());
        holder.tvAuthor.setTextColor(fontColor);
        holder.tvType.setText(book.getbType() == null ? "书籍类型" : book.getbType());
        holder.tvType.setTextColor(fontColor);
        holder.tvLastUpdateTime.setText(book.getbLastUpdateTime() == null ? "最后更新时间" : book.getbLastUpdateTime());
        holder.tvLastUpdateTime.setTextColor(fontColor);
        holder.tvIntroduction.setText(book.getbIntroduction() == null ? "简介" : book.getbIntroduction());
        holder.tvIntroduction.setTextColor(fontColor);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public void clear() {
        books.clear();
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemDetailsClickListener) {
        this.onItemClickListener = onItemDetailsClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llItemMain;
        private final ImageView ivCover;
        private final TextView tvName;
        private final TextView tvAuthor;
        private final TextView tvType;
        private final TextView tvLastUpdateTime;
        private final TextView tvIntroduction;

        public ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.isb_iv_cover_frame).setBackgroundResource(R.drawable.cover_frame);
            this.llItemMain = view.findViewById(R.id.isb_ll_item_main);
            this.ivCover = view.findViewById(R.id.isb_iv_cover);
            this.tvName = view.findViewById(R.id.isb_tv_name);
            this.tvAuthor = view.findViewById(R.id.isb_tv_author);
            this.tvType = view.findViewById(R.id.isb_tv_type);
            this.tvLastUpdateTime = view.findViewById(R.id.isb_tv_last_update_time);
            this.tvIntroduction = view.findViewById(R.id.isb_tv_introduction);
        }
    }
}
