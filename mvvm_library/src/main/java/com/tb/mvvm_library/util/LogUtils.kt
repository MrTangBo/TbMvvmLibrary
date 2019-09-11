package com.tb.mvvm_library.util

import android.util.Log
import com.tb.mvvm_library.base.TbConfigure

/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：日志工具类
 */
object LogUtils {

    fun log(str: String?) {
        if (TbConfigure.getInstance().isDebug) {
            if (str == null) {
                Log.i(TbConfigure.getInstance().logTag, "打印输入为null")
            } else {
                Log.i(TbConfigure.getInstance().logTag, str)
            }
        }
    }
}