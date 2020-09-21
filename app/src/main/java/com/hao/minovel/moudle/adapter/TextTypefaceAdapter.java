package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.List;


public class TextTypefaceAdapter extends RecyclerView.Adapter<TextTypefaceAdapter.NovelTextHolder> {
    private Context mContext;

    private List<TypeFaceUtils.TypeFaceInfo> typefaces = TypeFaceUtils.getTypeFaceInfoList();

    public TextTypefaceAdapter(Context context) {
        this.mContext = context;
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
        novelHolder.setDate(typefaces.get(i));
        novelHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(i, novelHolder.view, typefaces.get(i));
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public TextTypefaceAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return typefaces == null ? 0 : typefaces.size();
    }

    public TypeFaceUtils.TypeFaceInfo getItem(int index) {
        try {
            return typefaces.get(index);
        } catch (Exception e) {
            return null;
        }
    }


    class NovelTextHolder extends RecyclerView.ViewHolder {

        TextView typeFace;
        View view;

        public NovelTextHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            typeFace = itemView.findViewById(R.id.title_item);
        }

        public void setDate(TypeFaceUtils.TypeFaceInfo typeface) {
            typeFace.setTypeface(typeface.getTypeface());
            typeFace.setText(typeface.getTypeFacename());
        }
    }
}
