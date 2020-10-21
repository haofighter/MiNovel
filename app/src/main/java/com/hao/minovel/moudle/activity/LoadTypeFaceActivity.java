package com.hao.minovel.moudle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;
import com.hao.minovel.moudle.adapter.LoadTypefaceAdapter;
import com.hao.minovel.utils.SystemUtil;

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

    @Override
    protected int layoutId() {
        return R.layout.activity_load_type_face;
    }

    @Override
    protected void initView(View v) {
        ((TextView) v.findViewById(R.id.title)).setText("字体");
        v.findViewById(R.id.back).setOnClickListener(this);
        RecyclerView layoutContent = v.findViewById(R.id.layout_content);
        layoutContent.setLayoutManager(new LinearLayoutManager(this));
        layoutContent.setAdapter(new LoadTypefaceAdapter(this));
    }

    @Override
    protected void doElse() {
        downloadTypefaceConfigFile();
    }

    @Override
    protected void doOnSetContent(View v) {
        int padding = (int) SystemUtil.dp2px(this, 10f);
        v.findViewById(R.id.layout_top).setPadding(padding, padding+SystemUtil.getStatusBarHeight(this), padding, padding);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void downloadTypefaceConfigFile() {
        new OkHttpClient().newCall(new Request.Builder().get().url("https://raw.githubusercontent.com/haofighter/MiNovel/master/somethingNeeddown/FS.jpg").build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("下载请求", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //拿到字节流
                InputStream is = response.body().byteStream();
                int len = 0;
                byte[] buf = new byte[128];
                String str = "";
                while ((len = is.read(buf)) != -1) {
                    str += new String(buf);
                }
                is.close();
                Log.i("下载请求", str);
            }
        });
    }
}