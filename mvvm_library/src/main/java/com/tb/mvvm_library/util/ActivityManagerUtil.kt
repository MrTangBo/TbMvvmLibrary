package com.tb.mvvm_library.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.*


/**
 * @author: TB
 * @package: com.tb.design.library.tbActivityUtil
 * @description:
 * @date: 2018/11/14
 * @time: 16:51
 */
class ActivityManagerUtil {

    private var mActivities: Stack<Activity>? = null

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance: ActivityManagerUtil = ActivityManagerUtil()
    }

    //添加activity
    fun addActivity(activity: Activity) {
        if (mActivities != null) {
            mActivities!!.add(activity)
        } else {
            mActivities = Stack()
        }
    }

    //关闭指定的Activity
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            if (mActivities!!.contains(activity)) {
                mActivities!!.remove(activity)
            }
            activity.finish()
        }
    }

    //将activity全部关闭掉
    fun clearAll() {
        for (activity in mActivities!!) {
            activity.finish()
        }
    }

    //将activity全部关闭掉,activityName
    fun clearOther(activityName: String) {
        for (activity in mActivities!!) {
            if (activity.javaClass.simpleName == activityName) {
                continue
            }
            activity.finish()
        }
    }

    //获取Activity栈
    fun getStack(): Stack<Activity> {
        return mActivities!!
    }

    //判断某一个类是否存在任务栈里面
    fun isExistMainActivity(activity: Class<*>, context: Context): Boolean {
        val intent = Intent(context, activity)
        val cmpName = intent.resolveActivity(context.packageManager)
        return cmpName != null
    }


}
