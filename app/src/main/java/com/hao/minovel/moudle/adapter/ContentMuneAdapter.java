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

import com.hao.minovel.R;
import com.hao.minovel.moudle.entity.ContentMuneEntity;

import java.util.ArrayList;
import java.util.List;

public class ContentMuneAdapter extends RecyclerView.Adapter<ContentMuneAdapter.MuneViewHolder> {
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

    public MuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MuneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.mune_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MuneViewHolder holder, int position) {
        holder.setDate(muneEntityList.get(position), onClickListener, position);
    }

    @Override
    public int getItemCount() {
        return muneEntityList.size();
    }

    class MuneViewHolder extends RecyclerView.ViewHolder {
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
}
