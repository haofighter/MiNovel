package com.hao.roundconrtolview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity2 extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = "MainActivity2";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity");
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });
        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity2");
                startActivity(new Intent(MainActivity2.this, MainActivity2.class));
            }
        });
        findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity3");
                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            }
        });
        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BaseActivity_" + tag, "跳转MainActivity4");
                startActivity(new Intent(MainActivity2.this, MainActivity4.class));
            }
        });
    }
}