package com.example.secondactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hao.annotetion.annotation.Bind

@Bind(path = "secondactivity/MainActivity")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}