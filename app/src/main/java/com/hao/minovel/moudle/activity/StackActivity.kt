package com.hao.minovel.moudle.activity

import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.hao.annotetion.annotation.Bind
import com.hao.minovel.R
import com.hao.minovel.db.DBManage
import com.hao.minovel.moudle.adapter.StackPageAdapter
import com.hao.minovel.moudle.adapter.StackMuneAdapter
import com.hao.minovel.moudle.entity.StackTypeEntity
import com.hao.minovel.moudle.fragment.StackFragment
import com.hao.minovel.moudle.service.ServiceManage
import com.hao.minovel.spider.data.NovelType
import com.hao.minovel.tinker.app.AppContext
import com.hao.minovel.utils.SystemUtil
import com.hao.minovel.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_stack.*
import kotlinx.android.synthetic.main.head_view.*
import java.lang.Exception

/**
 * 书库
 */
@Bind
class StackActivity : MiMuneActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.book_icon -> drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    override fun layoutContentId(): Int {
        return R.layout.activity_stack
    }

    override fun doOnSetContent(v: View) {
        super.doOnSetContent(v)
        val padding = SystemUtil.dp2px(this, 10f).toInt()
        v.findViewById<View>(R.id.title_item).setPadding(padding, SystemUtil.getStatusBarHeight(this) + padding, padding, padding)
    }

    override fun initView(v: View?) {
        super.initView(v)
        //取消空白部分灰色阴影效果
        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent))
        book_icon.setOnClickListener(this)
        stack.visibility = View.GONE

        mune.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        var novelTypes: List<NovelType> = DBManage.getNovelType()
        mune.adapter = StackMuneAdapter(this, novelTypes, View.OnClickListener { v ->
            show_stack.currentItem = v.tag as Int
            changePage(v.tag as Int)
            drawerLayout.closeDrawers()
        })


        var stackTypeEntitys = mutableListOf<StackTypeEntity>()
        for ((index, e) in novelTypes.withIndex()) {
            stackTypeEntitys.add(StackTypeEntity(index, e.type, StackFragment.newInstance(e)))
        }
        show_stack.adapter = StackPageAdapter(supportFragmentManager, stackTypeEntitys)
        show_stack.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                changePage(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        changePage(show_stack.currentItem)
    }


    override fun doElse() {
        dodrawerDrag()
    }


    private fun changePage(index: Int) {
        try {
            title_name.text = (show_stack.adapter as StackPageAdapter).getmFragments()[index].muneItemm
            (mune.adapter as StackMuneAdapter).setCheck(index)
        } catch (e: Exception) {
            ToastUtils.showMessage("加载书库失败")
            ServiceManage.getInstance().startBackRunService(AppContext.application)
        }
    }

}
