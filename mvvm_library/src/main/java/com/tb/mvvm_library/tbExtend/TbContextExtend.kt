package com.tb.mvvm_library.tbExtend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.hipermission.HiPermission
import com.tb.mvvm_library.hipermission.PermissionCallback
import com.tb.mvvm_library.hipermission.PermissionItem
import com.tb.mvvm_library.tbEntity.TbApkInfo
import com.tb.mvvm_library.util.SystemBarUtil
import java.io.*

/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：自定义扩展方法
 */

/*********Context类的扩展*********/
/*获取dimension资源文件*/
fun Any.tbGetDimensValue(dimensionId: Int): Int {
    return TbApplication.mApplicationContext.resources.getDimension(dimensionId).toInt()
}

/*获取屏幕尺寸IntArray[0]为宽度 IntArray[1]为高度*/
fun Any.tbGetScreenSize(): IntArray {
    val wm = TbApplication.mApplicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
}

/*打开软键盘和关闭软键盘 isOpen=false关闭 为true打开*/
fun Context.tbKeyboard(isOpen: Boolean = false) {
    if (this is Activity) {
        if (isOpen) {
            Handler().postDelayed({
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

            }, 500)
        } else {
            try {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/*存储序列化对象*/
fun Any.tbSaveObject(`object`: Serializable, fileName: String) {
    var fos: ObjectOutputStream? = null
    val mContext: Context = TbApplication.mApplicationContext
    try {

        //如果文件不存在就创建文件
        val file = File(mContext.cacheDir, fileName)
        //file.createNewFile();
        //获取输出流
        //这里如果文件不存在会创建文件，这是写文件和读文件不同的地方
        fos = ObjectOutputStream(FileOutputStream(file))
        //这里不能再用普通的write的方法了
        //要使用writeObject
        fos.writeObject(`object`)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos?.close()
        } catch (e: IOException) {
        }
    }
}

/*读取序列化对象*/
fun Any.tbReadObject(fileName: String): Serializable? {
    var ois: ObjectInputStream? = null
    val mContext: Context = TbApplication.mApplicationContext
    try {
        //获取输入流
        ois = ObjectInputStream(FileInputStream(File(mContext.cacheDir, fileName)))
        //获取文件中的数据
        var people = ois.readObject()
        //把数据显示在TextView中
        return people as Serializable
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            ois?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    return null
}

/*跳转界面*/
fun Any.tbStartActivity(
    clazz: Class<*>,
    params: MutableMap<String, Serializable>? = null,
    requestCode: Int? = null,
    activityOptions: Bundle? = ActivityOptionsCompat.makeCustomAnimation(
        TbApplication.mApplicationContext, R.anim.slide_right_in, R.anim.slide_left_out
    ).toBundle()
) {
    if (this !is Activity && (this !is Fragment)) {
        tbShowToast("该Context不支持跳转")
        return
    }
    var currentActivity: Activity? = null
    if (this is Activity) {
        currentActivity = this
    } else if (this is Fragment) {
        currentActivity = this.activity!!
    }
    if (currentActivity == null) {
        tbShowToast("该Activity不能为空")
        return
    }

    if (!params.isNullOrEmpty()) {
        val b = Bundle()
        params.forEach {
            b.putSerializable(it.key, it.value)
        }
        if (requestCode != null) {
            currentActivity.startActivityForResult(
                Intent(currentActivity, clazz).putExtras(b),
                requestCode,
                activityOptions
            )
        } else {
            currentActivity.startActivity(Intent(currentActivity, clazz).putExtras(b), activityOptions)
        }
    } else {
        if (requestCode != null) {
            currentActivity.startActivityForResult(Intent(currentActivity, clazz), requestCode, activityOptions)
        } else {
            currentActivity.startActivity(Intent(currentActivity, clazz), activityOptions)
        }
    }
}

/*设置状态栏
* statusColorId状态栏颜色
*NavigationBarColorId 底部导航栏颜色
* isImmersive 是否是沉浸式模式
* isLightMode 状态栏TextColor和图标颜色是否为黑色（用于实现默写设计状态栏为白底黑子）
* isFitWindowStatusBar 是否填充状态栏
* */
fun Context.tbStatusBarInit(
    statusColorId: Int = R.color.tb_green,
    navigationBarColorId: Int = R.color.tb_black,
    isImmersive: Boolean = false,
    isLightMode: Boolean = false,
    isFitWindowStatusBar: Boolean = false
) {
    if (this !is Fragment && this !is Activity) return
    var currentActivity: Activity? = null
    if (this is Activity) {
        currentActivity = this
    } else if (this is Fragment) {
        currentActivity = this.activity!!
    }
    if (currentActivity == null) return
    val window = currentActivity.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = ContextCompat.getColor(this, statusColorId)
    window.navigationBarColor = ContextCompat.getColor(this, navigationBarColorId)
    val decorView = window.decorView
    when {
        isImmersive -> decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        else -> SystemBarUtil.statusBarLightMode(currentActivity, isFitWindowStatusBar, isLightMode)
    }
}

/*权限初始化*/
fun Context.tbRequestPermission(
    permissionItems: ArrayList<PermissionItem>?,
    styleId: Int = R.style.PermissionDefaultGreenStyle,
    colorId: Int = R.color.tb_green,
    title: String = "请设置权限",
    msg: String = "为了正常使用请开启权限",
    permissionSuccess: TbOnClick = null,//获取权限
    permissionFault: TbOnClick = null
) {
    if (permissionItems == null) return
    if (permissionItems.isEmpty()) return
    HiPermission.create(this)
        .title(title)
        .permissions(permissionItems)
        .filterColor(ContextCompat.getColor(this, colorId))
        .msg(msg)
        .style(styleId).checkMutiPermission(object : PermissionCallback {
            override fun onClose() {
                tbShowToast("用户关闭权限")
                permissionFault?.invoke()
            }

            override fun onFinish() {
                permissionSuccess?.invoke()
            }

            override fun onDeny(permission: String, position: Int) {

            }

            override fun onGuarantee(permission: String, position: Int) {
            }
        })
}

/*app详细信息*/
fun Any.tbApkInfo(): TbApkInfo {
    val mActivity = TbApplication.mApplicationContext
    val apkInfo = TbApkInfo()
    val packageManager = mActivity.packageManager
    val packageInfo = packageManager.getPackageInfo(mActivity.packageName, 0)
    apkInfo.apkName = mActivity.resources.getString(packageInfo.applicationInfo.labelRes)
    apkInfo.packgeName = mActivity.packageName
    apkInfo.versionCode = packageInfo.versionCode
    apkInfo.versionName = packageInfo.versionName
    return apkInfo
}













