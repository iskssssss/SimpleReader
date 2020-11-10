package com.xrzx.reader.view.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xrzx.commonlibrary.entity.TypefaceEntity;
import com.xrzx.reader.R;
import com.xrzx.reader.view.base.BaseRecyclerView;

import java.util.ArrayList;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/10 18:42
 */
public class TypefaceAdapter extends BaseRecyclerView<TypefaceAdapter.ViewHolder> {

    private final int resourceId;
    private int fontColor;
    private final Context context;
    private final ArrayList<TypefaceEntity> typefaces;
    private TypefaceEntity currTypefaces;

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public void setCurrTypefaces(TypefaceEntity currTypefaces) {
        this.currTypefaces = currTypefaces;
    }

    public TypefaceEntity getCurrTypefaces() {
        return currTypefaces;
    }

    public void setCurrTypefacesId(int id) {
        for (TypefaceEntity typeface : typefaces) {
            if (typeface.getId() == id) {
                this.currTypefaces = typeface;
            }
        }
    }

    public TypefaceAdapter(Context context, int resourceId, ArrayList<TypefaceEntity> typefaces) {
        this.context = context;
        this.resourceId = resourceId;
        this.typefaces = typefaces;
        this.fontColor = context.getColor(R.color.colorReadFontColorBlack);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypefaceEntity typefaceEntity = typefaces.get(position);
        holder.llItemMain.setOnClickListener(v -> onItemClickListener.onClick(typefaceEntity, position));
        holder.tvSelectStatus.setText((currTypefaces.equals(typefaceEntity)) ? "✔" : "");
        holder.tvSelectStatus.setTextColor(this.fontColor);
        holder.tvName.setText(typefaceEntity.getName());
        holder.tvName.setTextColor(this.fontColor);
        Typeface typeface = typefaceEntity.getId() == 0 ? Typeface.SANS_SERIF : this.context.getResources().getFont(typefaceEntity.getResourceNumber());
        holder.tvName.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return typefaces.size();
    }

    @Override
    public void clear() {
        typefaces.clear();
        currTypefaces = null;
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llItemMain;
        private TextView tvName;
        private TextView tvSelectStatus;

        public ViewHolder(View view) {
            super(view);
            this.llItemMain = view.findViewById(R.id.it_ll_item_main);
            this.tvName = view.findViewById(R.id.it_tv_name);
            this.tvSelectStatus = view.findViewById(R.id.it_tv_select_status);
        }
    }
}