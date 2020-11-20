package com.hao.minovel.moudle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;
import com.hao.minovel.base.MiBaseActivity;

@Bind(path = "app/SettingActivity")
public class SettingActivity extends MiBaseActivity {



    @Override
    protected int layoutId() {
        return R.layout.mirecyckerview;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void doElse() {

    }

    @Override
    protected void doOnSetContent(View v) {

    }
}
