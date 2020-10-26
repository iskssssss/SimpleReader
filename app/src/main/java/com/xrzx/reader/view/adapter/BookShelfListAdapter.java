package com.xrzx.reader.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xrzx.reader.R;
import com.xrzx.reader.book.entity.Book;

import java.util.List;

/**
 * @Description 书架书籍列表适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class BookShelfListAdapter extends ArrayAdapter<Book> {
    private int resourceID;

    public BookShelfListAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book chapterInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iVCover = view.findViewById(R.id.bsbi_iVCover);
            viewHolder.tVName = view.findViewById(R.id.bsbi_tVName);
            viewHolder.tVAuthor = view.findViewById(R.id.bsbi_tVAuthor);
            viewHolder.tVLastUpdateTime = view.findViewById(R.id.bsbi_tVLastUpdateTime);
            viewHolder.tVLastUpdateChapter = view.findViewById(R.id.bsbi_tVLastUpdateChapter);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.iVCover.setBackgroundResource(R.drawable.cover);
        viewHolder.tVName.setText(chapterInfo.getbName());
        viewHolder.tVAuthor.setText(chapterInfo.getbAuthor());
        viewHolder.tVLastUpdateTime.setText(chapterInfo.getbLastUpdateTime());
        viewHolder.tVLastUpdateChapter.setText(chapterInfo.getbLastUpdateChapter());
        return view;
    }

    private static class ViewHolder {
        private ImageView iVCover;
        private TextView tVName;
        private TextView tVAuthor;
        private TextView tVLastUpdateTime;
        private TextView tVLastUpdateChapter;
    }
}
