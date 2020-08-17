package com.hao.minovel.moudle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hao.annotetion.annotation.Bind;
import com.hao.minovel.R;

@Bind
public class MiShiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_shift);
    }
}
