package com.hao.roundconrtolview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public String tag = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取activity任务栈
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        Log.i("BaseActivity_" + tag, "onCreate(@Nullable Bundle savedInstanceState)    " + info.id + "        " + this.toString());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("BaseActivity_" + tag, "onSaveInstanceState(@NonNull Bundle outState)");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("BaseActivity_" + tag, "onNewIntent(Intent intent)");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i("BaseActivity_" + tag, "onPostCreate(@Nullable Bundle savedInstanceState)");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("BaseActivity_" + tag, "onConfigurationChanged(@NonNull Configuration newConfig)");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("BaseActivity_" + tag, "onPostResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BaseActivity_" + tag, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BaseActivity_" + tag, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("BaseActivity_" + tag, "onDestroy()");
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        Log.i("BaseActivity_" + tag, "onTitleChanged(CharSequence title, int color)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("BaseActivity_" + tag, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BaseActivity_" + tag, "onResume() ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i("BaseActivity_" + tag, "onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)");
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("BaseActivity_" + tag, "onRestoreInstanceState(@NonNull Bundle savedInstanceState)");
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        Log.i("BaseActivity_" + tag, "onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)");
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        Log.i("BaseActivity_" + tag, "onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BaseActivity_" + tag, "onRestart()");
    }

}
