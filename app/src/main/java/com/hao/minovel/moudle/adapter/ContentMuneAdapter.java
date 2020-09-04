package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.hao.minovel.R;
import com.hao.minovel.moudle.entity.ContentMuneEntity;

import java.util.ArrayList;
import java.util.List;

public class ContentMuneAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<ContentMuneEntity> muneEntityList = new ArrayList<>();
    View.OnClickListener onClickListener;


    public ContentMuneAdapter(Context mContext, List<ContentMuneEntity> muneEntityList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.muneEntityList = muneEntityList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.logo, null));
        } else {
            return new MuneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.mune_item, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {

        } else {
            int index = position - 1;
            ((MuneViewHolder) holder).setDate(muneEntityList.get(index), onClickListener, index);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return muneEntityList.size() + 1;
    }

    class MuneViewHolder extends ViewHolder {
        TextView textView;
        ImageView imageView;
        View itemView;

        public MuneViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.item_show_img);
            this.textView = itemView.findViewById(R.id.item_name_tv);
        }

        public void setDate(ContentMuneEntity contentMuneEntity, View.OnClickListener onClickListener, int index) {
            textView.setText(contentMuneEntity.getMuneItemm());
            imageView.setBackgroundResource(contentMuneEntity.getItmeImgId());
            itemView.setTag(index);
            itemView.setOnClickListener(onClickListener);
        }
    }

    class HeadViewHolder extends ViewHolder {

        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
