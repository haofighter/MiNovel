package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseAdapter;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.spider.data.NovelTypeHot;
import com.hao.minovel.tinker.app.AppContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 小说列表
 */
public class NovelListAdapter extends MiBaseAdapter<RecyclerView.ViewHolder> {
    Context context;


    int count;

    /**
     * 是否需要动画
     */
    boolean isNeedLoadAnimal = false;


    /**
     * 小说类型
     */
    //TODO 通过加载的网页进行控制
    String type = "";

    MuneAdapterListener muneAdapterListener;
    ArrayList<NovelIntroduction> novelIntroductions = new ArrayList<NovelIntroduction>() {
        @Override
        public void add(int index, NovelIntroduction novelIntroduction) {
            List<NovelIntroduction> needRemove = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                if (novelIntroduction.getNovelChapterListUrl().equals(get(i).getNovelChapterListUrl())) {
                    needRemove.add(get(i));
                }
            }
            super.add(index, novelIntroduction);
            removeAll(needRemove);
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends NovelIntroduction> c) {
            List<NovelIntroduction> needRemove = new ArrayList<>();
            for (NovelIntroduction nov : c) {
                boolean isContent = false;
                for (int i = 0; i < size(); i++) {
                    if (nov.getNovelChapterListUrl().equals(get(i).getNovelChapterListUrl())) {
                        isContent = true;
                        set(i, nov);
                    }
                }
                if (isContent) {
                    needRemove.add(nov);
                }
            }
            c.removeAll(needRemove);
            return super.addAll(c);
        }
    };

    AdapterView.OnItemClickListener onItemClickListener;

    public NovelListAdapter(Context context, boolean isNeedLoadAnimal) {
        this.context = context;
        this.isNeedLoadAnimal = isNeedLoadAnimal;
        if (isNeedLoadAnimal) {
            isOpen = true;
        }
    }


    public void setDate(List<NovelIntroduction> novelIntros) {
        novelIntroductions.clear();
        novelIntroductions.addAll(novelIntros);
        count = novelIntroductions == null ? 0 : novelIntroductions.size();
        notifyDataSetChanged();
    }

    public void addDate(List<NovelIntroduction> novelIntros) {
        novelIntroductions.addAll(novelIntros);
        count = novelIntroductions.size();
        notifyDataSetChanged();
    }


    public NovelIntroduction getItem(int i) {
        return novelIntroductions.get(i);
    }


    public boolean isOpen() {
        return isOpen;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(context).inflate(R.layout.novel_list_item, null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new HotNovelViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.novel_list_item2, null);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new NearUpViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (novelIntroductions.get(position).isIshot()) {
            return 1;
        } else {
            return 2;
        }
    }

    public void setItemOnClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isNeedLoadAnimal) {
            startAnimal(holder, position);
        }
        holder.itemView.setTag(position);
        NovelIntroduction novelIntroduction = novelIntroductions.get(position);
        if (novelIntroduction.isIshot()) {
            ((HotNovelViewHolder) holder).setDate(novelIntroduction);
        } else {
            ((NearUpViewHolder) holder).setDate(novelIntroduction);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void changeDisOrHide() {
        if (!isInAnimal) {
            isOpen = !isOpen;
            notifyDataSetChanged();
        }
    }


    public void setType(String type) {
        this.type = type;
    }

    public void addHead(List<NovelTypeHot> checkedNovelHotByType) {
        for (int i = 0; i < checkedNovelHotByType.size(); i++) {
            NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(checkedNovelHotByType.get(i).getChapterlistUrl());
            novelIntroduction.setIshot(true);
            novelIntroductions.add(0, novelIntroduction);
        }
        notifyDataSetChanged();
    }


    class HotNovelViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView novel_avatar;
        TextView novel_name;
        TextView novel_author;
        TextView novel_new;
        CardView card_view;
        int imageWidth;
        int imageHight;

        public HotNovelViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            novel_avatar = view.findViewById(R.id.novel_avatar);
            card_view = view.findViewById(R.id.card_view);
            novel_name = view.findViewById(R.id.novel_name);
            novel_author = view.findViewById(R.id.novel_author);
            novel_new = view.findViewById(R.id.novel_new);
        }

        public void setDate(NovelIntroduction novelIntroductions) {
            view.setBackgroundResource(R.color.transparent);
            novelIntroductions = DBManage.checkNovelByUrl(novelIntroductions.getNovelChapterListUrl());
            if (novelIntroductions.getNovelCover() != null) {
                Glide.with(AppContext.application).load(novelIntroductions.getNovelCover()).error(R.mipmap.novel_normal_cover).into(novel_avatar);
            } else {
                Glide.with(AppContext.application).load(R.mipmap.novel_normal_cover).into(novel_avatar);
            }
            novel_name.setText(novelIntroductions.getNovelName());
            novel_author.setText("作者：" + novelIntroductions.getNovelAutho());
            novel_new.setText("最新章节：" + novelIntroductions.getNovelNewChapterTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) {
                        return;
                    }
                    onItemClickListener.onItemClick(null, view, (Integer) view.getTag(), view.getId());
                }
            });
        }

        public void setSize(View frisetitemView) {
            if (frisetitemView != null) {
                imageWidth = frisetitemView.getWidth();
                imageHight = frisetitemView.getHeight();
            }
        }
    }

    class NearUpViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView novel_name;
        TextView novel_author;
        TextView novel_new;
        CardView card_view;

        public NearUpViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            card_view = view.findViewById(R.id.card_view);
            novel_name = view.findViewById(R.id.novel_name);
            novel_author = view.findViewById(R.id.novel_author);
            novel_new = view.findViewById(R.id.novel_new);
        }

        public void setDate(NovelIntroduction novelIntroductions) {
            view.setBackgroundResource(R.color.transparent);
            novelIntroductions = DBManage.checkNovelByUrl(novelIntroductions.getNovelChapterListUrl());
            novel_name.setText(novelIntroductions.getNovelName());
            novel_author.setText("作者：" + novelIntroductions.getNovelAutho());
            novel_new.setText("最新章节：" + novelIntroductions.getNovelNewChapterTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) {
                        return;
                    }
                    onItemClickListener.onItemClick(null, view, (Integer) view.getTag(), view.getId());
                }
            });
        }
    }

}
