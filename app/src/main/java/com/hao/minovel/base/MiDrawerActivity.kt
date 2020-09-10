package com.hao.minovel.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.hao.minovel.R
import com.hao.minovel.view.RoundLayout

public abstract class MiDrawerActivity : MiBaseActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var contentShow: RoundLayout

    override fun layoutId(): Int {
        return R.layout.activity_mi_drawer
    }

    abstract fun layoutContentId(): Int
    abstract fun layoutDrawerId(): Int

    override fun doElse() {

    }

    override fun doOnSetContent(v: View) {
        drawerLayout = v.findViewById(R.id.drawer_layout)
        contentShow = v.findViewById(R.id.content_show)
        contentShow.addView(LayoutInflater.from(this).inflate(layoutContentId(), null))
        var drawer = LayoutInflater.from(this).inflate(layoutDrawerId(), null)
        var layoutParam = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        drawer.layoutParams = layoutParam
        v.findViewById<RoundLayout>(R.id.content_drawer).addView(drawer)
    }


}
