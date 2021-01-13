package com.hao.roundconrtolview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = "MainActivity";
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        ControlSurfaceView controlSurfaceView = findViewById(R.id.controlsurfaceview);
//        controlSurfaceView.addMainButton(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
//        controlSurfaceView.addView(LayoutInflater.from(this).inflate(R.layout.testviewlayout, null));
        findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity");
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity2");
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity3");
                startActivity(new Intent(MainActivity.this, MainActivity3.class));
            }
        });
        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity4");
                startActivity(new Intent(MainActivity.this, MainActivity4.class));
            }
        });
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