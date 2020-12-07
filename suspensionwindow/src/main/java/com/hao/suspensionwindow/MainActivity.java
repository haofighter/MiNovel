    package com.hao.suspensionwindow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hao.annotetion.annotation.Bind;
import com.hao.suspensionwindow.suspension.FloatingImageDisplayService;
import com.hao.suspensionwindow.suspension.FloatingVideoService;

import java.util.HashMap;

@Bind(path = "suspensionwindow/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "suspensionwindow");
        setContentView(R.layout.activity_main12314);

    }






}