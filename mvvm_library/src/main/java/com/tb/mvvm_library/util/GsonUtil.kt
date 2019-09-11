package com.tb.mvvm_library.util

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 *@作者：tb
 *@时间：2019/6/28
 *@描述：Gson工具类
 */
class GsonUtil {

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = GsonUtil()
    }

    /**/
    var mGson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()

    //转换为字符串
    fun <T> toJson(t: T): String = mGson.toJson(t)

    //转换为对象
    fun <T> fromJson(t: String, clazz: Class<T>): T = mGson.fromJson(t, clazz)

    //转换为集合
    fun <T> fromJsonList(t: String, type: Type): T {
        return mGson.fromJson(t, type)
    }
}