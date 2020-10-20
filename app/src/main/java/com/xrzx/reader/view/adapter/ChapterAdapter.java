package com.xrzx.reader.view.adapter;
import com.xrzx.reader.book.entity.ChapterInfo;
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

public class ChapterAdapter extends ArrayAdapter<ChapterInfo> {
    private int resourceID;

    public ChapterAdapter(@NonNull Context context, int resource, @NonNull List<ChapterInfo> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChapterInfo chapterInfo = getItem(position);
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
        viewHolder.cTitle.setText(chapterInfo.getcTitle());
        return view;
    }

    private static class ViewHolder {
        private TextView cTitle;
    }
}
