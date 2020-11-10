package com.xrzx.reader.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.reader.R;
import com.xrzx.commonlibrary.entity.Book;
import com.xrzx.reader.view.base.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 书架书籍列表适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfListAdapter extends BaseRecyclerView<BookShelfListAdapter.ViewHolder> {

    private final int resourceId;
    private final Context context;
    private final ArrayList<Book> books;

    public BookShelfListAdapter(Context context, int resourceId, ArrayList<Book> books) {
        this.context = context;
        this.resourceId = resourceId;
        this.books = books;
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
        holder.tvAuthor.setText(book.getbAuthor());
        holder.tvLastUpdateTime.setText(book.getbLastUpdateTime());
        holder.tvLastUpdateChapter.setText(book.getbLastUpdateChapter());
        holder.llDetails.setOnClickListener(v -> onItemDetailsClickListener.onClick(position));
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

        private LinearLayout llItemMain;
        private ImageView ivCover;
        private TextView tvName;
        private TextView tvAuthor;
        private TextView tvLastUpdateTime;
        private TextView tvLastUpdateChapter;
        private LinearLayout llDetails;

        public ViewHolder(View view) {
            super(view);
            view.findViewById(R.id.ibs_iv_cover_frame).setBackgroundResource(R.drawable.cover_frame);
            this.llItemMain = view.findViewById(R.id.ibs_ll_item_main);
            this.ivCover = view.findViewById(R.id.ibs_iv_cover);
            this.tvName = view.findViewById(R.id.ibs_tv_name);
            this.tvAuthor = view.findViewById(R.id.ibs_tv_author);
            this.tvLastUpdateTime = view.findViewById(R.id.ibs_tv_last_update_time);
            this.tvLastUpdateChapter = view.findViewById(R.id.ibs_tv_last_update_chapter);
            this.llDetails = view.findViewById(R.id.ibs_ll_details);
        }
    }
}
