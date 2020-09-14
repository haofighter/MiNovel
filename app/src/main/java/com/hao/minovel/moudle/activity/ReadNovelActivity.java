package com.hao.minovel.moudle.activity;


import android.view.View;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;

import org.jetbrains.annotations.Nullable;


@Bind
public class ReadNovelActivity extends MiMuneActivity {

    @Override
    public int layoutDrawerId() {
        return R.layout.show_chapters;
    }

    @Override
    public int layoutContentId() {
        return R.layout.activity_read_novel;
    }

    @Override
    protected void initView(@Nullable View v) {
        super.initView(v);
    }
}
