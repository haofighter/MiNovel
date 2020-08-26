package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hao.minovel.R;
import com.hao.minovel.spider.data.NovelIntroduction;

import java.util.List;


public class TextNovelAdapter extends RecyclerView.Adapter<TextNovelAdapter.NovelTextHolder> {
    private Context mContext;

    private List<NovelIntroduction> mNovelPage;
    private List<String> history;

    private boolean isShowHistory = false;

    public TextNovelAdapter(Context context) {
        this.mContext = context;
    }

    public boolean isShowHistory() {
        return isShowHistory;
    }

    public void setmNovelPage(List<NovelIntroduction> novelPage) {
        this.isShowHistory = false;
        this.mNovelPage = novelPage;
        notifyDataSetChanged();
    }

    public void setShowHistory(List<String> history) {
        this.isShowHistory = true;
        this.history = history;
        notifyDataSetChanged();
    }

    public Object getDate() {
        if (isShowHistory) {
            return history;
        } else {
            return mNovelPage;
        }
    }


    @NonNull
    @Override
    public NovelTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new NovelTextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NovelTextHolder novelHolder, final int i) {
        if (isShowHistory) {
            novelHolder.setDate(history.get(i));
        } else {
            novelHolder.setDate(mNovelPage.get(i));
        }
        novelHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (isShowHistory) {
                        onItemClickListener.itemClick(i, novelHolder.view, history.get(i));
                    } else {
                        onItemClickListener.itemClick(i, novelHolder.view, mNovelPage.get(i));
                    }
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public TextNovelAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        if (isShowHistory) {
            return history == null ? 0 : history.size();
        } else {
            return mNovelPage == null ? 0 : mNovelPage.size();
        }
    }

    public void setList(List<NovelIntroduction> novelListItemContents) {
        mNovelPage = novelListItemContents;
        notifyDataSetChanged();
    }

    class NovelTextHolder extends RecyclerView.ViewHolder {

        TextView novel_name;
        View view;

        public NovelTextHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            novel_name = itemView.findViewById(R.id.title_item);
        }

        public void setDate(NovelIntroduction novelClassify) {
            if (novelClassify.getNovelAutho() == null) {
                novelClassify.setNovelAutho("");
            }
            String auther = novelClassify.getNovelAutho() == null ? "" :
                    (novelClassify.getNovelAutho().equals("") ? "" : ("(" + novelClassify.getNovelAutho() + ")"));
            novel_name.setText(novelClassify.getNovelName() + auther);
        }

        public void setDate(String string) {
            novel_name.setText(string);
        }
    }

    public interface OnItemClickListener {
        void itemClick(int position, View view, Object object);
    }
}
