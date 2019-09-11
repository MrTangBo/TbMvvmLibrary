package com.tb.test

import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.base.TbConfigure
import okhttp3.logging.HttpLoggingInterceptor

class MyApplication : TbApplication() {

    override fun onCreate() {
        super.onCreate()
        TbConfigure.getInstance().setError(R.mipmap.ic_launcher).setPlaceholder(R.mipmap.ic_launcher)
            .setBaseUrl("http://v.juhe.cn")
            .setOkHttpClient(HttpLoggingInterceptor.Level.BODY)
    }
}