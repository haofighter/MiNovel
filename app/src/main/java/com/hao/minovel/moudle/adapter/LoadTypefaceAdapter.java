package com.hao.minovel.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;
import com.hao.minovel.utils.TypeFaceUtils;

import java.util.List;


public class LoadTypefaceAdapter extends RecyclerView.Adapter<LoadTypefaceAdapter.TypeFaceSrcHolder> {
    private Context mContext;


    public LoadTypefaceAdapter(Context context) {
        this.mContext = context;
    }


    @NonNull
    @Override
    public TypeFaceSrcHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_typeface_load, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new TypeFaceSrcHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeFaceSrcHolder novelHolder, final int i) {

    }

    private OnItemClickListener onItemClickListener;
    public LoadTypefaceAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return 0;
    }




    class TypeFaceSrcHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;

        public TypeFaceSrcHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.typeface_show);
        }

    }
}
