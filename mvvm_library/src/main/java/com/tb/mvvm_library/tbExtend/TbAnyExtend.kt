package com.tb.mvvm_library.tbExtend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.tbDialog.TbSureDialog
import com.tb.mvvm_library.tbShareUser.ShareUserInfoUtil
import com.tb.mvvm_library.util.ActivityManagerUtil


/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：自定义扩展方法
 */

/*保存缓存SharedPreferences*/
fun Any?.tbSetShared(key: String, isClean: Boolean = true) {
    if (this == null) return
    val share = ShareUserInfoUtil.getInstance(isClean)
    when (this) {
        is String -> share.putString(key, this)
        is Int -> share.putInt(key, this)
        is Float -> share.putFloat(key, this)
        is Long -> share.putLong(key, this)
        is Boolean -> share.putBoolean(key, this)
    }
}

/*获取String缓存SharedPreferences*/
@Suppress("UNCHECKED_CAST")
fun <T> Any.tbGetShared(key: String, clazz: Class<T>, isClean: Boolean = true): T {
    val share = ShareUserInfoUtil.getInstance(isClean)
    return when (clazz) {
        String::class.java -> share.getString(key, "") as T
        Int::class.java -> share.getInt(key, 0) as T
        Float::class.java -> share.getFloat(key, 0f) as T
        Boolean::class.java -> share.getBoolean(key, false) as T
        else -> "" as T
    }
}

/*扩展Toast土司*/
fun Any.tbShowToast(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    @DrawableRes background: Int = TbConfigure.getInstance().toastBg,
    @LayoutRes layoutId: Int = TbConfigure.getInstance().toastLayoutId
) {
    val mToast = Toast(TbApplication.mApplicationContext)
    val view = LayoutInflater.from(TbApplication.mApplicationContext).inflate(layoutId, null)
    val toastBackground = view.findViewById<LinearLayout>(R.id.toastLinear)
    toastBackground.background = ContextCompat.getDrawable(TbApplication.mApplicationContext, background)
    val textView = view.findViewById<TextView>(R.id.toast_text)
    textView.text = msg
    mToast.duration = Toast.LENGTH_SHORT
    mToast.setGravity(gravity, 0, tbGetDimensValue(R.dimen.x80))
    mToast.view = view
    mToast.show()
}

/* 添加activity*/
fun Activity.tbAddActivity() {
    ActivityManagerUtil.getInstance().addActivity(this)
}

/*关闭指定的Activity*/
fun Any.tbRemoveActivity(activityName: String) {
    ActivityManagerUtil.getInstance().clearOther(activityName)
}

/*将activity全部关闭掉*/
fun Any.tbCleanAllActivity() {
    ActivityManagerUtil.getInstance().clearAll()
}

/*获取手机唯一标识MEID*/
@SuppressLint("HardwareIds")
fun Any.tbGetPhoneOnlyNum(context: Context): String {
    val androidID = Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
    val serialNumber = android.os.Build.SERIAL
    val id = androidID + serialNumber
    return id.tb2Md5()
}


/*网络是否可用*/
fun Any.tbNetWorkIsConnect(): Boolean {
    val mConnectManager: ConnectivityManager =
        TbApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netWorkInfo = mConnectManager.activeNetworkInfo
    if (netWorkInfo != null && netWorkInfo.isConnected) {
        return (netWorkInfo.state == NetworkInfo.State.CONNECTED)
    }
    return false
}

/* 判断当前网络类型*/
fun Any.tbNetWorkIsWifi(): Boolean {
    val mConnectManager: ConnectivityManager =
        TbApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netWorkInfo = mConnectManager.activeNetworkInfo
    if (netWorkInfo != null && netWorkInfo.isConnected) {
        return netWorkInfo.type == ConnectivityManager.TYPE_WIFI
    }
    return false
}

/* 判断当前网络类型*/
fun Any.tbNetWorkIsMobile(): Boolean {
    val mConnectManager: ConnectivityManager =
        TbApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netWorkInfo = mConnectManager.activeNetworkInfo
    if (netWorkInfo != null && netWorkInfo.isConnected) {
        return netWorkInfo.type == ConnectivityManager.TYPE_MOBILE
    }
    return false
}

/*获取状态栏和导航栏的高度*/
@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun Any.tbStatusBarHeight(): IntArray {
    val array: IntArray = IntArray(2)
    var statusBarHeight = -1
    var navigationbarHeight = -1
    val resources: Resources = TbApplication.mApplicationContext.resources
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val any = clazz.newInstance()
        val height = Integer.parseInt(clazz.getField("status_bar_height").get(any).toString())
        statusBarHeight = resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val resIdShow = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    var hasNavigationBar = false
    if (resIdShow > 0) {
        hasNavigationBar = resources.getBoolean(resIdShow)//是否显示底部navigationBar
    }
    if (hasNavigationBar) {
        val resIdNavigationBar = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        navigationbarHeight = 0
        if (resIdNavigationBar > 0) {
            navigationbarHeight = resources.getDimensionPixelSize(resIdNavigationBar)//navigationBar高度
        }
    }
    array[0] = statusBarHeight
    array[1] = navigationbarHeight
    return array
}

/*获取通知栏权限并设置*/
fun Any.tbNotifyEnabled(activity: AppCompatActivity? = null, messageTx: String = "应用需要通知权限，立即去设置？"): Boolean {
    val appInfo = TbApplication.mApplicationContext.applicationInfo
    val pkg = TbApplication.mApplicationContext.applicationContext.packageName
    val uid = appInfo.uid
    if (!NotificationManagerCompat.from(TbApplication.mApplicationContext).areNotificationsEnabled()) {
        if (activity != null) {
            val sureDialog = TbSureDialog(messageTx = messageTx)
            sureDialog.sureClick = {
                val localIntent = Intent()
                localIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//>=8.0
                    localIntent.action =Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg)
                } else {
                    localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    localIntent.data = Uri.fromParts("package", pkg, null)
                    localIntent.putExtra("app_uid", uid)
                }
                activity.startActivity(localIntent)
            }
            sureDialog.show(activity.supportFragmentManager, "notification")
        } else {
            tbShowToast("应用需要通知权限，请到设置中心设置！")
        }
    } else {
        return true
    }
    return false
}



























