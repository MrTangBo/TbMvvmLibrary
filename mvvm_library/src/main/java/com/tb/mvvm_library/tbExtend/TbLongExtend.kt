package com.tb.mvvm_library.tbExtend

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：自定义扩展方法
 */

/*********extra类的扩展*********/

/*将毫秒时间戳转为时间字符串*/
fun Long?.tbMillis2String(defaultPattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    if (this == null) return ""
    return SimpleDateFormat(defaultPattern, Locale.getDefault()).format(Date(this))
}

/*将时间戳转为Date类型*/
fun Long?.tbMillis2Date(): Date? {
    if (this == null) return null
    return Date(this)
}

/**
 * 获取两个时间的时间差
 * precision=1 返回天
 * precision=2 返回天和小时
 * precision=3 返回天、小时和分钟
 * precision=4 返回天、小时、分钟和秒
 */
fun Long?.tbGetTimeSpan(millis: Long, precision: Int = 4): String {
    if (this == null) {
        return "0"
    }
    var span = Math.abs(this - millis)
    if (span == 0L) return "0"
    val sb = StringBuilder()
    val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
    val unitLen = intArrayOf(86400000, 3600000, 60000, 1000, 1)
    for (i in 0 until precision) {
        if (span >= unitLen[i]) {
            val mode = span / unitLen[i]
            span -= mode * unitLen[i]
            sb.append(mode).append(units[i])
        }
    }
    return sb.toString()
}

/*将时间戳转为 月 天 时 分 秒*/
@Suppress("UNREACHABLE_CODE")
fun Long?.tbMillis2Time(unit: TimeUnit): String {
    if (this == null) return ""
    return return when (unit) {
        TimeUnit.MILLISECONDS -> {
            this.toString().plus("毫秒")
        }
        TimeUnit.SECONDS -> {
            (this / 1000).toString().plus("秒")
        }
        TimeUnit.MINUTES -> {
            (this / 60000).toString().plus("分钟")
        }
        TimeUnit.HOURS -> {
            (this / 3600000).toString().plus("小时")
        }
        TimeUnit.DAYS -> {
            (this / 86400000).toString().plus("天")
        }
        else -> ""
    }
}

/*将Date转为时间戳类型*/
fun Date.tbDate2Millis(): Long {
    return this.time
}


























