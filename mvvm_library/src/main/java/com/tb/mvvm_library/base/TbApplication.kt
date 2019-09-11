package com.tb.mvvm_library.base

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import com.tb.mvvm_library.tbReceiver.TbBaseReceiver


/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：
 */
open class TbApplication : Application() {

    companion object {
        lateinit var mApplicationContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mApplicationContext = applicationContext
        initInternetReceiver()
    }


    open fun initInternetReceiver() {
        val receiver = TbBaseReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")//<!--网络状态改变广播-->
        registerReceiver(receiver, intentFilter)
    }
}