package com.tb.mvvm_library.httpUtil

import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.util.GsonUtil
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *@作者：tb
 *@时间：2019/6/28
 *@描述：构建Retrofit类 单例模式
 */
class RetrofitApi {

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = RetrofitApi()
    }

    fun <T> getInterface(
        clazz: Class<T>,
        converterFactory: Converter.Factory = GsonConverterFactory.create(GsonUtil.getInstance().mGson)
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(TbConfigure.getInstance().baseUrl)
            .client(TbConfigure.getInstance().okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(clazz)
    }
}