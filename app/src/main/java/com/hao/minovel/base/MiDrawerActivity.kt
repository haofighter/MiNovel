package com.hao.minovel.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.hao.minovel.R
import com.hao.minovel.view.RoundLayout
import kotlinx.android.synthetic.main.activity_mi_drawer.*

public abstract class MiDrawerActivity : MiBaseActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var contentShow: RoundLayout
    lateinit var contentDrawer: RoundLayout

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
        contentDrawer = v.findViewById(R.id.content_drawer)


        var layoutParam = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)


        var drawer = LayoutInflater.from(this).inflate(layoutDrawerId(), null)
        drawer.layoutParams = layoutParam
        contentDrawer.addView(drawer)

        var content = LayoutInflater.from(this).inflate(layoutContentId(), null)
        content.layoutParams = layoutParam
        contentShow.addView(content)


    }


}
