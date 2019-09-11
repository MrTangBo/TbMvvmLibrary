package com.tb.mvvm_library.tbReceiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import com.tb.mvvm_library.base.TbEventBusInfo
import com.tb.mvvm_library.tbExtend.tbNetWorkIsConnect
import com.tb.mvvm_library.tbExtend.tbNetWorkIsMobile
import com.tb.mvvm_library.tbExtend.tbNetWorkIsWifi
import com.tb.mvvm_library.tbExtend.tbShowToast
import com.tb.mvvm_library.util.LogUtils
import org.greenrobot.eventbus.EventBus

class TbBaseReceiver : BroadcastReceiver() {
    companion object {
        var isFirst: Boolean = true
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (tbNetWorkIsConnect()) {//判断网络是否可用
            LogUtils.log("onReceive--->")
            if (!isFirst) {
                val b = Bundle()
                b.putString("flag", TbBaseReceiver::class.java.simpleName)
                EventBus.getDefault().post(TbEventBusInfo(b))
                if (tbNetWorkIsMobile()) {
                    tbShowToast("当前正在使用移动网络！")
                }
            }
            isFirst = false
        } else {
            when {
                tbNetWorkIsWifi() -> {
                    tbShowToast("当前wifi网络不可用！")
                    isFirst = false
                }
                tbNetWorkIsMobile() -> {
                    isFirst = false
                    tbShowToast("当前移动网络不可用！")
                }
                else -> {
                    isFirst = false
                    tbShowToast("当前网络不可用！")
                }
            }
        }
    }
}
