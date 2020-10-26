package com.xrzx.reader.view.adapter;
import com.xrzx.reader.book.entity.Chapter;
import com.xrzx.reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @Description 章节适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ChapterAdapter extends ArrayAdapter<Chapter> {
    private int resourceID;

    public ChapterAdapter(@NonNull Context context, int resource, @NonNull List<Chapter> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Chapter chapter = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cTitle = view.findViewById(R.id.ci_tVName);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.cTitle.setText(chapter.getcTitle());
        return view;
    }

    private static class ViewHolder {
        private TextView cTitle;
    }
}
