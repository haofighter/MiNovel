package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.minovel.R;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.entity.ReadInfo;
import com.hao.minovel.spider.data.NovelChapter;
import com.hao.minovel.spider.data.NovelIntroduction;
import com.hao.minovel.tinker.app.AppContext;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class ShiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private boolean showDelete = false;

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        notifyDataSetChanged();
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    private List<ReadInfo> readInfos = DBManage.checkedAllReadInfo();

    public ShiftAdapter(Context context) {
        this.mContext = context;
    }

    public void refresh() {
        readInfos = DBManage.checkedAllReadInfo();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bookshelf_lastest, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new MiNearReadHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bookshelf_other, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new OtherNovelHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder novelHolder, int i) {
        if (i == 0) {
            if (readInfos.size() > 0) {
                ((MiNearReadHolder) novelHolder).setDate(readInfos.get(i), showDelete);
                ((MiNearReadHolder) novelHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ShiftAdapter.this.showDelete = true;
                        ShiftAdapter.this.notifyDataSetChanged();
                        return true;
                    }
                });
            }
        } else {
            if (i * 3 + 1 > readInfos.size()) {
                ((OtherNovelHolder) novelHolder).setDate(readInfos.subList((i - 1) * 3 + 1, readInfos.size()), showDelete);
            } else {
                ((OtherNovelHolder) novelHolder).setDate(readInfos.subList((i - 1) * 3 + 1, i * 3 + 1), showDelete);
            }
        }


//        novelHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
////                    onItemClickListener.itemClick(i, novelHolder.view, typefaces.get(i));
//                }
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private OnItemClickListener onItemClickListener;

    public ShiftAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (readInfos == null || readInfos.size() == 0) {
            size = 1;
        } else {
            if (readInfos.size() > 1) {
                size = (readInfos.size() - 1) % 3 == 0 ? ((readInfos.size() - 1) / 3 + 1) : ((readInfos.size() - 1) / 3 + 2);
            } else {
                size = readInfos.size();
            }
        }
        return size;
    }

    public ReadInfo getItem(int index) {
        try {
            return readInfos.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public void notifiDate() {
        readInfos = DBManage.checkedAllReadInfo();
        notifyDataSetChanged();
    }


    class MiNearReadHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView novel_name;
        TextView tv_durprogress;
        TextView tv_watch;
        View itemView;
        View delete;

        public MiNearReadHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            iv_cover = itemView.findViewById(R.id.iv_cover);
            novel_name = itemView.findViewById(R.id.novel_name);
            tv_durprogress = itemView.findViewById(R.id.tv_durprogress);
            tv_watch = itemView.findViewById(R.id.tv_watch);
            delete = itemView.findViewById(R.id.delete);
            tv_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadInfo readInfo = (ReadInfo) v.getTag();
                    if (tv_watch.getText().equals("继续阅读")) {
//                        Intent intent = new Intent(mContext, ReadNovelActivity.class);
//                        intent.putExtra("novelChapter", DbManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl()));
//                        mContext.startActivity(intent);
                    } else {
                        EventBus.getDefault().post("toStack");
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ReadInfo readInfo = (ReadInfo) v.getTag();
//                    Intent intent = new Intent(mContext, NovelDetailActivity.class);
//                    intent.putExtra("novelId", readInfo.getNovelChapterListUrl());
//                    ViewCompat.setTransitionName(iv_cover, "cover");
//                    Pair<View, String> pair1 = new Pair<>((View) iv_cover, ViewCompat.getTransitionName(iv_cover));
//                    /**
//                     *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
//                     */
//                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
//                    ActivityCompat.startActivity(mContext, intent, activityOptionsCompat.toBundle());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DbManage.removeReadInfo(DbManage.checkedAllReadInfo().get(0));
//                    notifiDate();
                }
            });

        }

        public void setDate(ReadInfo readInfo, boolean showDelete) {
            if (showDelete) {
                delete.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }
            NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(readInfo.getNovelChapterListUrl());
            NovelChapter novelChapter = DBManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
            if (novelIntroduction != null) {
                novel_name.setText("《" + novelIntroduction.getNovelName() + "》");
                Log.i("封面地址", novelIntroduction.getNovelCover() + "   " + novelIntroduction.isComplete() + "   更新时间：" + novelIntroduction.getCreatTime());
                Glide.with(AppContext.application).load(novelIntroduction.getNovelCover()).error(R.mipmap.back_1).into(iv_cover);
            }
            if (novelChapter != null) {
                tv_durprogress.setText(novelChapter.getChapterName());
            }
            tv_watch.setTag(readInfo);
            itemView.setTag(readInfo);
            if (showDelete) {
                delete.setVisibility(View.VISIBLE);
            }

        }
    }

    private class OtherNovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        RelativeLayout fl_content_1;
        ImageView iv_cover_1;
        TextView tv_name_1;
        ImageView delete_1;
        RelativeLayout fl_content_2;
        ImageView iv_cover_2;
        TextView tv_name_2;
        ImageView delete_2;
        RelativeLayout fl_content_3;
        ImageView iv_cover_3;
        TextView tv_name_3;
        ImageView delete_3;

        public OtherNovelHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            fl_content_1 = itemView.findViewById(R.id.fl_content_1);
            iv_cover_1 = itemView.findViewById(R.id.iv_cover_1);
            tv_name_1 = itemView.findViewById(R.id.tv_name_1);
            delete_1 = itemView.findViewById(R.id.delete_1);
            fl_content_2 = itemView.findViewById(R.id.fl_content_2);
            iv_cover_2 = itemView.findViewById(R.id.iv_cover_2);
            tv_name_2 = itemView.findViewById(R.id.tv_name_2);
            delete_2 = itemView.findViewById(R.id.delete_2);
            fl_content_3 = itemView.findViewById(R.id.fl_content_3);
            iv_cover_3 = itemView.findViewById(R.id.iv_cover_3);
            tv_name_3 = itemView.findViewById(R.id.tv_name_3);
            delete_3 = itemView.findViewById(R.id.delete_3);
        }

        public void setDate(List<ReadInfo> readInfo, boolean showDelete) {
            iv_cover_1.setOnClickListener(this);
            iv_cover_2.setOnClickListener(this);
            iv_cover_3.setOnClickListener(this);
            if (showDelete) {
                delete_1.setVisibility(View.VISIBLE);
                delete_2.setVisibility(View.VISIBLE);
                delete_3.setVisibility(View.VISIBLE);
            } else {
                delete_1.setVisibility(View.GONE);
                delete_2.setVisibility(View.GONE);
                delete_3.setVisibility(View.GONE);
            }
            fl_content_1.setVisibility(View.INVISIBLE);
            fl_content_2.setVisibility(View.INVISIBLE);
            fl_content_3.setVisibility(View.INVISIBLE);

            for (int j = 0; j < readInfo.size(); j++) {
                NovelIntroduction novelIntroduction = DBManage.checkNovelByUrl(readInfo.get(j).getNovelChapterListUrl());
                TextView title = null;
                ImageView cover = null;
                RelativeLayout layout = null;
                ImageView delete = null;
                switch (j) {
                    case 0:
                        iv_cover_1.setTag(readInfo.get(j));
                        title = tv_name_1;
                        cover = iv_cover_1;
                        layout = fl_content_1;
                        delete_1.setTag(readInfo.get(j));
                        delete = delete_1;
                        break;
                    case 1:
                        iv_cover_2.setTag(readInfo.get(j));
                        title = tv_name_2;
                        cover = iv_cover_2;
                        layout = fl_content_2;
                        delete_2.setTag(readInfo.get(j));
                        delete = delete_2;
                        break;
                    case 2:
                        iv_cover_3.setTag(readInfo.get(j));
                        title = tv_name_3;
                        cover = iv_cover_3;
                        layout = fl_content_3;
                        delete_3.setTag(readInfo.get(j));
                        delete = delete_3;
                        break;
                    default:
                        break;
                }

                if (novelIntroduction != null) {
                    if (title != null) {
                        title.setText(novelIntroduction.getNovelName());
                    }
                    if (cover != null) {
                        Glide.with(AppContext.application).load(novelIntroduction.getNovelCover()).error(R.mipmap.back_1).into(cover);
                    }
                    if (layout != null) {
                        layout.setVisibility(View.VISIBLE);
                    }
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBManage.removeReadInfo((ReadInfo) v.getTag());
                        notifiDate();
                    }
                });

                cover.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ShiftAdapter.this.showDelete = true;
                        ShiftAdapter.this.notifyDataSetChanged();
                        return true;
                    }
                });

            }
        }

        @Override
        public void onClick(View v) {
            ReadInfo readInfo = (ReadInfo) v.getTag();
            NovelChapter novelChapter = DBManage.getChapterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
//TODO 跳转小说详情界面
            //            Intent intent = new Intent(mContext, ReadNovelActivity.class);
//            intent.putExtra("novelChapter", novelChapter);
//            mContext.startActivity(intent);
//            Intent intent = new Intent(mContext, NovelDetailActivity.class);
//            intent.putExtra("novelId", readInfo.getNovelChapterListUrl());
//            Pair<View, String> pair1 = null;
//            switch (v.getId()) {
//                case R.id.iv_cover_1:
//                    ViewCompat.setTransitionName(iv_cover_1, "cover");
//                    pair1 = new Pair<>((View) iv_cover_1, ViewCompat.getTransitionName(iv_cover_1));
//                    break;
//                case R.id.iv_cover_2:
//                    ViewCompat.setTransitionName(iv_cover_2, "cover");
//                    pair1 = new Pair<>((View) iv_cover_2, ViewCompat.getTransitionName(iv_cover_2));
//                    break;
//                case R.id.iv_cover_3:
//                    ViewCompat.setTransitionName(iv_cover_3, "cover");
//                    pair1 = new Pair<>((View) iv_cover_3, ViewCompat.getTransitionName(iv_cover_3));
//                    break;
//            }
//            if (pair1 != null) {
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
//                ActivityCompat.startActivity(mContext, intent, activityOptionsCompat.toBundle());
//            } else {
//                App.getInstance().startActivity(intent);
//            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(int position, View view, Object object);
    }


}
