package com.hao.minovel.moudle.activity

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.hao.annotationengine.Router
import com.hao.annotetion.annotation.Bind
import com.hao.minovel.R
import com.hao.minovel.db.DBManage
import com.hao.minovel.log.MiLog
import com.hao.minovel.moudle.adapter.StackMuneAdapter
import com.hao.minovel.moudle.adapter.StackPageAdapter
import com.hao.minovel.moudle.entity.StackTypeEntity
import com.hao.minovel.moudle.fragment.StackFragment
import com.hao.minovel.moudle.service.LoadHtmlService
import com.hao.minovel.moudle.service.LoadWebInfo
import com.hao.minovel.moudle.service.ServiceManage
import com.hao.minovel.spider.SpiderUtils
import com.hao.minovel.spider.data.NovelType
import com.hao.minovel.tinker.app.AppContext
import com.hao.minovel.utils.SystemUtil
import kotlinx.android.synthetic.main.activity_stack.*
import kotlinx.android.synthetic.main.head_view.*

/**
 * 书库
 */
@Bind(path = ActivityConfig.STACKACTIVITY)
class StackActivity : MiMuneActivity(), View.OnClickListener {
    private var loadWebInfo = LoadWebInfo()

    override fun onClick(v: View) {
        when (v.id) {
            R.id.book_icon -> drawerLayout.openDrawer(Gravity.LEFT)
            R.id.setting -> Router.getInstance().build(ActivityConfig.SETTINGACTIVITY).skip()
            R.id.stack -> Router.getInstance().build(ActivityConfig.SEARCHNOVELACTIVITY).skip()
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
        setView()
    }

    private fun setView() {
        var novelTypes: List<NovelType> = DBManage.groupNovelType()
        if (novelTypes.isEmpty() || novelTypes.size < 5) {
            var intent = Intent(this, LoadHtmlService::class.java)
            loadWebInfo.task = LoadHtmlService.noveltype;
            intent.putExtra(LoadHtmlService.TASK, loadWebInfo)
            startService(intent)
        } else {
            //取消空白部分灰色阴影效果
            drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.transparent))
            book_icon.setOnClickListener(this)
            stack.setImageResource(R.mipmap.icon_search)
            mune.layoutManager = LinearLayoutManager(this)
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
    }

    override fun eventBusOnEvent(str: LoadWebInfo?) {
        if (str!!.id.equals(loadWebInfo.id)) {
            MiLog.i("加载完成");
            if (str.loadStatus == SpiderUtils.Success) {
                setView()
            }
        }
        super.eventBusOnEvent(str)
    }

    override fun doElse() {
        dodrawerDrag()
    }

    private fun changePage(index: Int) {
        try {
            title_name.text = (show_stack.adapter as StackPageAdapter).getmFragments()[index].muneItemm
            (mune.adapter as StackMuneAdapter).setCheck(index)
        } catch (e: Exception) {
            Toast.makeText(this@StackActivity, "错误：加载书库失败", Toast.LENGTH_LONG).show();
            ServiceManage.getInstance().startSearchAllnovleTitle(AppContext.application)
        }
    }

}
