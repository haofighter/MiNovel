package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.List;


public class NovelChapterAdapter extends RecyclerView.Adapter<NovelChapterAdapter.NovelChapterHolder> {
    private Context mContext;
    private String selectUrl = "";

    private List<NovelChapter> novelChapters = new ArrayList<>();

    public NovelChapterAdapter(Context context) {
        this.mContext = context;
    }


    public void setNovelChapters(NovelChapter novelChapter) {
        this.novelChapters = DBManage.checkedNovelList(novelChapter.getNovelChapterListUrl());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NovelChapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new NovelChapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NovelChapterHolder novelHolder, final int i) {
        novelHolder.setDate(novelChapters.get(i));
        novelHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(i, novelHolder.view, novelChapters.get(i));
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public NovelChapterAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return novelChapters == null ? 0 : novelChapters.size();
    }

    public NovelChapter getItem(int index) {
        try {
            return novelChapters.get(index);
        } catch (Exception e) {
            return null;
        }
    }


    public void setSelect(String chapterUrl) {
        this.selectUrl = chapterUrl;
        notifyDataSetChanged();
    }


    class NovelChapterHolder extends RecyclerView.ViewHolder {

        TextView title_item;
        View view;

        public NovelChapterHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title_item = itemView.findViewById(R.id.title_item);
            title_item.setLines(1);
        }

        public void setDate(NovelChapter novelChapter) {
            title_item.setText(novelChapter.getChapterName());
            if (novelChapter.getChapterUrl().equals(selectUrl)) {
                view.setBackgroundResource(R.color.yellow);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        }
    }

}
