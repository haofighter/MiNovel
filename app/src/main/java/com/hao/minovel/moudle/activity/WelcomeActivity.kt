package com.hao.minovel.moudle.activity

import android.Manifest
import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import com.hao.annotationengine.Router
import com.hao.annotetion.annotation.Bind

import com.hao.minovel.base.MiBaseActivity
import com.hao.minovel.utils.BackCall
import com.hao.minovel.utils.DialogUtils
import com.hao.minovel.utils.SystemUtil
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.util.Pair
import com.hao.minovel.log.MiLog
import com.hao.minovel.moudle.service.LoadWebInfo
import com.hao.minovel.moudle.service.ServiceManage
import com.hao.minovel.tinker.app.AppContext
import com.hao.minovel.utils.SystemConfigUtil


@Bind(path = "app/WelcomeActivity")
class WelcomeActivity : MiBaseActivity() {

    override fun doOnSetContent(v: View?) {
    }

    override fun eventBusOnEvent(o: Any?) {
    }


    /**
     * 判断动画是否在运行中
     */
    private var animalIsStart = false

    /**
     * 是否以跳转 防止重复跳转
     */
    private var isJumpMian = false

    /**
     * 当前页面执行的动画
     */
    private var valueAnimator: ValueAnimator = ValueAnimator.ofFloat(100f)

    /**
     * 当前页面执行的动画
     */
    private var promisstion: Boolean = false

    /**
     *
     */
    private var dialog: Dialog? = null;

    override fun beforOnCreate(): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            // 隐藏状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏导航栏
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)//隐藏状态栏
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or flags
        }
        return super.beforOnCreate()
    }

    override fun layoutId(): Int {
        return com.hao.minovel.R.layout.activity_main
    }

    override fun initView(v: View) {
//        novel_icon.visibility = View.VISIBLE
//        logo_text.visibility = View.VISIBLE
//        logo_advert.visibility = View.VISIBLE
        logo_text.setOnClickListener { gotoMain() }
    }

    override fun doElse() {
        initAnimal()
        initDialog()
        initPromision()

    }

    override fun onResume() {
        super.onResume()
        initPromision()
    }

    private fun initDialog() {
        if (dialog == null) {
            dialog = DialogUtils.showInfoDialog(this, "提示", "您还未开启储存权限，是否设置", "去设置", "退出", object : BackCall<Int> {
                override fun call(t: Int) {
                    when (t) {
                        com.hao.minovel.R.id.confirm -> {
                            SystemUtil.getPhoneFrom(this@WelcomeActivity)
                        }
                        com.hao.minovel.R.id.cancel -> {
                            finish()
                        }
                    }
                }
            })
        }
    }

    private fun initPromision() {
        val promissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)

        if (!dialog?.isShowing!!) {
            promisstion = initPromission(promissions)
            if (promisstion) {
                SystemConfigUtil.getInstance().initDynamicShortcuts()
                //开启小说服务 进行该网站小说遍历
                ServiceManage.getInstance().startSearchAllnovleTitle(AppContext.application)
                if (!valueAnimator.isRunning && !isJumpMian) {
                    gotoMain()
                }
            }
        }
    }

    override fun eventBusOnEvent(str: LoadWebInfo?) {
        MiLog.i("小说服务：" + str!!.task + "    加载状态 " + str!!.loadStatus)
        super.eventBusOnEvent(str)
    }

    /**
     * 第一次进入时 申请权限 如果权限申请成功并且
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for ((index, permission) in permissions.withIndex()) {
            when (permission) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    if (grantResults[index] == -1) {
                        initDialog()
                        dialog?.show()
                    } else {
                        promisstion = promisstion && true
                        initPromision()
                    }
                }
            }
        }
    }


    fun initAnimal() {
        if (animalIsStart) {
            return
        }
        valueAnimator.duration = 2000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Float
            if (progress > 10 && progress < 75) {
                novel_icon.visibility = View.VISIBLE
                logo_text.visibility = View.VISIBLE
                logo_advert.visibility = View.VISIBLE
                novel_icon.translationX = novel_icon.width * (progress / 75 - 1)
                logo_text.alpha = progress / 75
            } else if (progress < 99) {
                logo_advert.visibility = View.VISIBLE
            } else if (progress == 100f) {
                if (valueAnimator.isRunning && !isJumpMian && promisstion) {
                    gotoMain()
                    isJumpMian = true
                }
                animalIsStart = false
            }
        }
        valueAnimator.start()
    }

    private fun gotoMain() {
        if (isJumpMian) {
            return
        }
        synchronized(this) {
            val pair1 = Pair<View, String>(novel_icon, ViewCompat.getTransitionName(novel_icon).toString())

            /**
             * 生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
             */
            val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1)
            val bundle = activityOptionsCompat.toBundle()
            bundle!!.putBoolean("animal", true)
            Router.getInstance().build("app/ShiftActivity", bundle, this).skip()
            //          ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
            //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //          ActivityCompat.startActivity(App.getInstance(), intent, bundle);
        }
    }


}