package com.hao.roundconrtolview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ControlSurfaceView controlSurfaceView = findViewById(R.id.controlsurfaceview);
        controlSurfaceView.addMainButton(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Log.i("触摸", "view");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    Log.i("触摸移动", "getLeft=" + view.getLeft() + movedX);
//                    view.layout(view.getLeft() + movedX, view.getTop() + movedY, view.getRight() + movedX, view.getBottom() + movedY);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}