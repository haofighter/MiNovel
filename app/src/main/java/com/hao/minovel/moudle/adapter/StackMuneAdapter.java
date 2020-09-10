package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelType;

import java.util.ArrayList;
import java.util.List;

public class StackMuneAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    List<NovelType> novelTypes = new ArrayList<>();
    View.OnClickListener onClickListener;
    int nowCheck;

    public StackMuneAdapter(Context mContext, List<NovelType> novelTypes, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.onClickListener = onClickListener;
        this.novelTypes = novelTypes;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.logo, null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new HeadViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.mune_item, null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new MuneViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {

        } else {
            int index = position - 1;
            ((MuneViewHolder) holder).setDate(novelTypes.get(index), onClickListener, index, nowCheck);
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
        return novelTypes.size() + 1;
    }


    class MuneViewHolder extends ViewHolder {
        TextView textView;
        ImageView imageView;
        View itemView;
        ImageView item_icon;

        public MuneViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.item_show_img);
            imageView.setVisibility(View.GONE);
            this.textView = itemView.findViewById(R.id.item_name_tv);
            this.item_icon = itemView.findViewById(R.id.item_icon);
        }

        public void setDate(NovelType novelType, View.OnClickListener onClickListener, int index, int nowCheck) {
            textView.setText(novelType.getType());
            textView.setTextColor(Color.BLACK);
            itemView.setTag(index);
            itemView.setOnClickListener(onClickListener);
            if (index == nowCheck) {
                itemView.setBackgroundResource(R.color.gray_11);
                item_icon.setVisibility(View.VISIBLE);
            } else {
                itemView.setBackgroundResource(R.color.transparent);
                item_icon.setVisibility(View.GONE);
            }
        }
    }

    class HeadViewHolder extends ViewHolder {
        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setCheck(int index) {
        this.nowCheck = index;
        notifyDataSetChanged();
    }
}
