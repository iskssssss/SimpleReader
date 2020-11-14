package com.xrzx.reader.adapter;
import com.xrzx.commonlibrary.entity.Chapter;
import com.xrzx.reader.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * @Description 章节适配器
 * @Author ks
 * @Date 2020/10/26 11:37
 */
public class ChapterAdapter extends ArrayAdapter<Chapter> {
    private int resourceID;
    private int fontColor;
    private int itemSelectChapterIndex = -1;

    public void setItemSelectChapterIndex(int itemSelectChapterIndex) {
        this.itemSelectChapterIndex = itemSelectChapterIndex;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public ChapterAdapter(@NonNull Context context, int resource, @NonNull List<Chapter> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.TextViewColor, typedValue, false)) {
            this.fontColor = context.getColor(typedValue.data);
        } else {
            this.fontColor = context.getColor(R.color.colorReadFontColorBlack);
        }
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
            viewHolder.tvTitle = view.findViewById(R.id.ic_tv_name);
            viewHolder.tvDownloadStatus = view.findViewById(R.id.ic_tv_download_status);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(chapter.getcTitle());
        viewHolder.tvDownloadStatus.setText(chapter.getcContent() != null ? "" : "未缓存");
        viewHolder.tvDownloadStatus.setTextColor(this.fontColor);
        if (itemSelectChapterIndex != -1) {
            final int color = position + 1 == itemSelectChapterIndex ? ContextCompat.getColor(getContext(), R.color.colorChapterSelect) : this.fontColor;
            viewHolder.tvTitle.setTextColor(color);
        }
        return view;
    }

    private static class ViewHolder {
        private TextView tvTitle;
        private TextView tvDownloadStatus;
    }
}
