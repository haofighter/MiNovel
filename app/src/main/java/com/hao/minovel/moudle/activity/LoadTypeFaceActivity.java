package com.hao.minovel.moudle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.db.DBManage;
import com.hao.minovel.moudle.adapter.LoadTypefaceAdapter;
import com.hao.minovel.moudle.adapter.OnItemClickListener;
import com.hao.minovel.moudle.adapter.TextTypefaceAdapter;
import com.hao.minovel.tinker.app.AppContext;
import com.hao.minovel.utils.SystemUtil;
import com.hao.minovel.utils.TypeFaceUtils;
import com.hao.minovel.view.minovelread.NovelTextDrawInfo;
import com.hao.minovel.view.recycleviewhelp.RecycleViewDivider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Bind
public class LoadTypeFaceActivity extends MiBaseActivity implements View.OnClickListener {
    NovelTextDrawInfo novelTextDrawInfo = DBManage.chackNovelConfig();
    RecyclerView typefaceList;
    TextView tvShow;
    SeekBar seekBar;

    @Override
    protected int layoutId() {
        return R.layout.activity_load_type_face;
    }

    @Override
    protected void initView(View v) {
        ((TextView) v.findViewById(R.id.title)).setText("字体");
        v.findViewById(R.id.back).setOnClickListener(this);
        typefaceList = findViewById(R.id.typeface_show);
        tvShow = findViewById(R.id.tv_show);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("小说", "进度：" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(60
        );
        initTypeFace();
    }


    @Override
    protected void doElse() {
        refreshView();
    }

    @Override
    protected void doOnSetContent(View v) {
        int padding = (int) SystemUtil.dp2px(this, 10f);
        v.findViewById(R.id.layout_top).setPadding(padding, padding + SystemUtil.getStatusBarHeight(this), padding, padding);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


    private void initTypeFace() {
        TextTypefaceAdapter textTypefaceAdapter = new TextTypefaceAdapter(this).setItemClickLisener(new OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, Object object) {
                novelTextDrawInfo.setTypeFaceName(((TypeFaceUtils.TypeFaceInfo) object).getTypeFacename());
                refreshView();
            }
        });

        typefaceList.setLayoutManager(new LinearLayoutManager(AppContext.application));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
        typefaceList.addItemDecoration(recycleViewDivider);
        typefaceList.setAdapter(textTypefaceAdapter);
    }

    private void refreshView() {
        ((TextTypefaceAdapter) typefaceList.getAdapter()).setSelect(novelTextDrawInfo.getTypeFaceName());
        tvShow.setTypeface(TypeFaceUtils.getTypeFaceByName(novelTextDrawInfo.getTypeFaceName()));
    }
}