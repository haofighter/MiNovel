package com.hao.minovel.base;


import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.minovel.R;

public abstract class MiBaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    /**
     * 是否正在执行动画
     */
    protected boolean isInAnimal = false;


    /**
     * view是否显示
     */
    protected boolean isOpen = false;

    MuneAdapterListener muneAdapterListener;

    public void setMuneAdapterListener(MuneAdapterListener muneAdapterListener) {
        this.muneAdapterListener = muneAdapterListener;
    }

    public interface MuneAdapterListener {
        void animalInEnd(int position);

        void animalOutEnd(int position);

    }

    public void startAnimal(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        isInAnimal = true;
        final Animation animationin = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_item_right_in);
        animationin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position == getItemCount() - 1) {
                    isInAnimal = false;
                    if (muneAdapterListener != null)
                        muneAdapterListener.animalInEnd(position);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final Animation animationout = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_item_out);
        animationout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position == 0) {
                    isInAnimal = false;
                    if (muneAdapterListener != null)
                        muneAdapterListener.animalOutEnd(position);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (isOpen) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        animationin.setDuration(100);
        animationout.setDuration(100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpen) {
                    holder.itemView.startAnimation(animationin);
                    holder.itemView.setVisibility(View.VISIBLE);
                } else {
                    holder.itemView.startAnimation(animationout);
                    holder.itemView.setVisibility(View.GONE);
                }
            }
        }, isOpen ? position * 100 : (getItemCount() - position - 1) * 100);
    }
}
