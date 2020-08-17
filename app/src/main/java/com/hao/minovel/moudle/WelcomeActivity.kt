package com.hao.minovel.moudle

import android.Manifest
import android.animation.ValueAnimator
import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.hao.annotationengine.Router
import com.hao.annotetion.annotation.Bind
import com.hao.date.RouterContent

import com.hao.minovel.R
import com.hao.minovel.base.MiBaseActivity
import com.hao.minovel.utils.BackCall
import com.hao.minovel.utils.DialogUtils
import com.hao.minovel.utils.SystemUtil
import kotlinx.android.synthetic.main.activity_main.*


@Bind
class WelcomeActivity : MiBaseActivity() {
    /**
     * 判断动画是否在运行中
     */
    private var animalIsStart = false
    /**
     * 是否以跳转 防止重复跳转
     */
    private var isJumpMian = false
    /**
     * 是否打开权限
     */
    private var promiss = false
    /**
     * 当前页面执行的动画
     */
    private var valueAnimator: ValueAnimator = ValueAnimator.ofFloat(100f)

    /**
     *
     */
    private var dialog: Dialog? = null;

    override fun beforOnCreate() {
        // 隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
//        novel_icon.visibility = View.VISIBLE
//        logo_text.visibility = View.VISIBLE
//        logo_advert.visibility = View.VISIBLE
    }

    override fun doElse() {
        initAnimal()
        initDialog()
        initPromision()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initDialog() {
        if (dialog == null) {
            dialog = DialogUtils.showInfoDialog(this, "提示", "您还未开启储存权限，是否设置", "去设置", "退出", object : BackCall<Int> {
                override fun call(t: Int) {
                    when (t) {
                        R.id.confirm -> {
                            SystemUtil.getPhoneFrom(this@WelcomeActivity)
                        }
                        R.id.cancel -> {
                            finish()
                        }
                    }
                }
            })
        }
    }

    private fun initPromision() {
        val promissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

        if (!dialog?.isShowing!!) {
            promiss = initPromission(promissions)
        }
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
                        promiss = true
                    }
                }
            }
        }
    }


    fun initAnimal() {
        if (animalIsStart) {
            return
        }
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Float
            Log.i("动画进度", "$progress   $isJumpMian")

            if (progress > 10 && progress < 75) {
                novel_icon.visibility = View.VISIBLE
                logo_text.visibility = View.VISIBLE
                logo_advert.visibility = View.VISIBLE
                novel_icon.translationX = novel_icon.width * (progress / 75 - 1)
                logo_text.alpha = progress / 75
            } else if (progress < 99) {
                logo_advert.visibility = View.VISIBLE
            } else if (progress == 100f) {
                Log.i("main", "animalIsStart=" + valueAnimator.isRunning + "   isJumpMian=" + isJumpMian + "   promiss=" + promiss);
                if (valueAnimator.isRunning && !isJumpMian && promiss) {
                    isJumpMian = true
                    gotoMain()
                }
                animalIsStart = false
            }
        }
        valueAnimator.start()
    }

    private fun gotoMain() {
        synchronized(this) {
            val pair1 = Pair<View, String>(novel_icon, ViewCompat.getTransitionName(novel_icon))
            /**
             * 生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
             */
            val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1)
            val bundle = activityOptionsCompat.toBundle()
            bundle!!.putBoolean("animal", true)
            Router.getInstance().build(RouterContent.MISHIFTACTIVITY, bundle, this).skip()

            //          ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
            //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //          ActivityCompat.startActivity(App.getInstance(), intent, bundle);

        }

    }

}
