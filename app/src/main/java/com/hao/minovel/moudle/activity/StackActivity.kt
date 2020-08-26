package com.hao.minovel.moudle.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hao.annotetion.annotation.Bind
import com.hao.minovel.R
import com.hao.minovel.base.MiBaseActivity

//书库
@Bind
class StackActivity : MiBaseActivity() {

    override fun beforOnCreate(): Boolean {
        return super.beforOnCreate()
    }

    override fun initView() {
    }

    override fun doElse() {
    }

    override fun layoutId(): Int {
        return R.layout.activity_stack;
    }


}
