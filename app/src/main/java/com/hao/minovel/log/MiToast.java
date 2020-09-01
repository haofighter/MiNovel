package com.hao.minovel.log;

import android.widget.Toast;

import com.hao.minovel.tinker.app.AppContext;

public class MiToast {
    public static void show(String string) {
        Toast.makeText(AppContext.application, string, Toast.LENGTH_LONG).show();
    }
}
