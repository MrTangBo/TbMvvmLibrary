package com.tb.mvvm_library.tbExtend

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import com.tb.mvvm_library.base.TbApplication

/**
 *@作者：tb
 *@时间：2019/8/30
 *@描述：系统控件的属性扩展
 */

//获取系统通知栏管理
val Any.tbNotificationManager: NotificationManager
    get() = TbApplication.mApplicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


