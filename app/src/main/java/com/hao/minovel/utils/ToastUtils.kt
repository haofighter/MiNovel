package com.hao.minovel.utils;

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.hao.minovel.tinker.app.AppContext

/**
 * Created by haozhang on 2018/1/3.
 */
object ToastUtils {
    private val handler = Handler(Looper.getMainLooper())

    private var toast: Toast? = null

    private val synObj = Any()

    fun showMessage(msg: String) {
        showMessage(msg, Toast.LENGTH_SHORT)
    }


    /**
     * 显示一个文本并且设置时长
     *
     * @param msg
     * @param len
     */
    fun showMessage(msg: CharSequence?, len: Int) {
        if (msg == null || msg == "") {
            Log.w("TOAST", "[ToastUtil] response message is null.")
            return
        }
        handler.post {
            synchronized(synObj) {
                //加上同步是为了每个toast只要有机会显示出来
                if (toast != null) {
                    //toast.cancel();
                    toast!!.setText(msg)
                    toast!!.duration = len
                } else {
                    toast = Toast.makeText(AppContext.application, msg, len)
                }
                toast!!.show()
            }
        }
    }
}