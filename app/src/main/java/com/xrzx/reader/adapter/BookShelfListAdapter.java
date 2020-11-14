package com.xrzx.reader.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.utils.ThemeUtils;
import com.xrzx.commonlibrary.view.custom.ThemeImageView;
import com.xrzx.reader.R;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.commonlibrary.view.base.BaseRecyclerView;

import java.util.ArrayList;

/**
 * @Description 书架书籍列表适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfListAdapter extends BaseRecyclerView<BookShelfListAdapter.ViewHolder> {

    private final int resourceId;
    private final Context context;
    private int fontColor;
    private Drawable drawable;
    private final ArrayList<Book> books;

    public BookShelfListAdapter(Context context, int resourceId, ArrayList<Book> books) {
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

        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[]{R.attr.MoreBackground});
        this.drawable = ta.getDrawable(0);
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
        holder.tvUpdate.setText((book.getbUpdate() == 1) ? "●" : "");
        holder.tvAuthor.setText(book.getbAuthor());
        holder.tvAuthor.setTextColor(fontColor);
        holder.tvLastUpdateTime.setText(book.getbLastUpdateTime());
        holder.tvLastUpdateTime.setTextColor(fontColor);
        holder.tvLastUpdateChapter.setText(book.getbLastUpdateChapter());
        holder.tvLastUpdateChapter.setTextColor(fontColor);
        holder.llDetails.setOnClickListener(v -> onItemDetailsClickListener.onClick(position));
        holder.ivMore.setBackground(drawable);
        holder.tvDelimiter.setTextColor(fontColor);
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

    OnItemDetailsClickListener onItemDetailsClickListener;

    public void setOnItemDetailsClickListener(OnItemDetailsClickListener onItemDetailsClickListener) {
        this.onItemDetailsClickListener = onItemDetailsClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llItemMain;
        private final ImageView ivCover;
        private final TextView tvName;
        private final TextView tvUpdate;
        private final TextView tvAuthor;
        private final TextView tvLastUpdateTime;
        private final TextView tvLastUpdateChapter;
        private final LinearLayout llDetails;
        private final ThemeImageView ivMore;
        private final TextView tvDelimiter;

        public ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.ibs_iv_cover_frame).setBackgroundResource(R.drawable.cover_frame);
            this.llItemMain = view.findViewById(R.id.ibs_ll_item_main);
            this.ivCover = view.findViewById(R.id.ibs_iv_cover);
            this.tvName = view.findViewById(R.id.ibs_tv_name);
            this.tvUpdate = view.findViewById(R.id.ibs_tv_update);
            this.tvAuthor = view.findViewById(R.id.ibs_tv_author);
            this.tvLastUpdateTime = view.findViewById(R.id.ibs_tv_last_update_time);
            this.tvLastUpdateChapter = view.findViewById(R.id.ibs_tv_last_update_chapter);
            this.llDetails = view.findViewById(R.id.ibs_ll_details);
            this.ivMore = view.findViewById(R.id.ibs_iv_more);
            this.tvDelimiter = view.findViewById(R.id.ibs_tv_delimiter);
        }
    }
}
