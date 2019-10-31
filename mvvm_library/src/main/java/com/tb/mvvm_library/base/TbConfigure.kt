package com.tb.mvvm_library.base

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import com.tb.mvvm_library.R
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class TbConfigure {

    var baseUrl: String = ""
    var isDebug: Boolean = true
    var logTag: String = "TB_TAG"
    var okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
    var placeholder: Int = 0
    var error: Int = 0
    var maxRetries: Int = 0 //最大重连数默认3秒
    var retryDelayMillis: Long = 8 //重连间隔默认8秒
    var cleanCacheName: String = "tb_clean_cache" //可以清除的缓存
    var cacheName: String = "tb_cache" //不可以清除的缓存
    var fontType: String = ""//字体样式设置（文件位于assets/fonts/my_font.ttf）
    var loadingLayoutId: Int = R.layout.dialog_loading//网络加载进度布局
    var loadingStyleId: Int = R.style.dialogProgress//网络加载进度样式
    @LayoutRes
    var toastLayoutId: Int = R.layout.tb_toast_style//toast 布局
    @DrawableRes
    var toastBg: Int = R.drawable.tb_bg_toast//toast 背景
    var requestMaxNum: Int = 3//同时请求最大数


    var emptyIcon: Int = R.drawable.icon_empty_data //空数据图标

    var noInternetIcon: Int = 0//无网络图标

    var errorIcon: Int = 0//加载出错图标


    fun setEmptyIcon(emptyIcon: Int): TbConfigure {
        this.emptyIcon = emptyIcon
        return this
    }

    fun setNoInternetIcon(noInternetIcon: Int): TbConfigure {
        this.noInternetIcon = noInternetIcon
        return this
    }

    fun setErrorIcon(errorIcon: Int): TbConfigure {
        this.errorIcon = errorIcon
        return this
    }

    companion object {
        fun getInstance() = Holder.instance
        //水平
        val HORIZONTAL_LIST = RecyclerView.HORIZONTAL
        //垂直
        val VERTICAL_LIST = RecyclerView.VERTICAL
        //水平+垂直
        val BOTH_SET = 2
    }

    private object Holder {
        val instance = TbConfigure()
    }


    fun setBaseUrl(baseUrl: String): TbConfigure {
        this.baseUrl = baseUrl
        return this
    }

    fun setPlaceholder(placeholder: Int): TbConfigure {
        this.placeholder = placeholder
        return this
    }

    fun setError(error: Int): TbConfigure {
        this.error = error
        return this
    }

    /*是否开启打印日志*/
    fun setIsDebug(isDebug: Boolean): TbConfigure {
        this.isDebug = isDebug
        return this
    }

    /*打印日志标签*/
    fun setMaxRetries(maxRetries: Int): TbConfigure {
        this.maxRetries = maxRetries
        return this
    }

    /*重链接次数*/
    fun setRetryDelayMillis(retryDelayMillis: Long): TbConfigure {
        this.retryDelayMillis = retryDelayMillis
        return this
    }

    /**
     * 设置OkHttpClient拦截器
     * loggingLevel:打印内容
     * timeOut:链接超时时间
     * headers:添加公共头部信息
     * certificatePinner:添加证书Pinning
     * sslList:本地证书放在Raw文件下面
     * */
    fun setOkHttpClient(
        loggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
        timeOut: Long = 15,
        isHostnameVerifier: Boolean = false,//是否信任所有服务器地址
        headers: MutableMap<String, String> = mutableMapOf(),
        certificate: MutableMap<String, String> = mutableMapOf(),
        sslList: ArrayList<Int> = arrayListOf()
    ): TbConfigure {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        /*设置链接超时*/
        okHttpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
        /*设置打印等级*/
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = loggingLevel
        okHttpClientBuilder.addNetworkInterceptor(loggingInterceptor)
        //信任所有服务器地址
        okHttpClientBuilder.hostnameVerifier { p0, p1 ->
            return@hostnameVerifier isHostnameVerifier
        }
        /*添加头部*/
        if (headers.isNotEmpty()) {
            val headerInterceptor = Interceptor { chain ->
                val requestHeader = chain.request().newBuilder()
                /*设置具体的header*/
                headers.forEach {
                    requestHeader.addHeader(it.key, it.value)
                }
                return@Interceptor chain.proceed(requestHeader.build())
            }
            okHttpClientBuilder.addInterceptor(headerInterceptor)
        }
        /*添加证书Pinning*/
        if (certificate.isNotEmpty()) {
            val certificatePinner = CertificatePinner.Builder()
            certificate.forEach {
                certificatePinner.add(it.key, it.value)
            }
            okHttpClientBuilder.certificatePinner(certificatePinner.build())
        }

        /*https绑定证书*/
        if (sslList.isNotEmpty()) {
            okHttpClientBuilder.sslSocketFactory(
                getInstance().getSSLSocketFactory(
                    TbApplication.mApplicationContext,
                    sslList
                )
            )
        }
        this.okHttpClient = okHttpClientBuilder.build()

        return this
    }

    /*设置默认加载图片*/
    fun initGlide(placeholder: Int = 0, error: Int = 0): TbConfigure {
        this.placeholder = placeholder
        this.error = error
        return this
    }


    /*设置字体路径*/
    fun setFontPath(fontTypePath: String): TbConfigure {
        fontType = fontTypePath
        return this

    }

    /*设置可清除缓存的名字*/
    fun setCleanCacheName(cleanCacheName: String): TbConfigure {
        this.cleanCacheName = cleanCacheName
        return this

    }

    /*设置可清除缓存的名字*/
    fun setacheName(cacheName: String): TbConfigure {
        this.cacheName = cacheName
        return this

    }

    /*设置网络加载进度布局*/
    fun setloadingLayoutId(@LayoutRes loadLayoutId: Int): TbConfigure {
        this.loadingLayoutId = loadLayoutId
        return this

    }

    /*设置网络加载进度样式*/
    fun setloadingStyle(@StyleRes styleId: Int): TbConfigure {
        this.loadingStyleId = styleId
        return this

    }

    /*设置Toast布局*/
    fun setToastLayoutId(@StyleRes toastLayoutId: Int): TbConfigure {
        this.toastLayoutId = toastLayoutId
        return this
    }

    /*设置Toast 背景*/
    fun setToastBg(@StyleRes toastBg: Int): TbConfigure {
        this.toastBg = toastBg
        return this
    }

    /*同时最大请求数*/
    fun setRequestMaxNum(requestMaxNum: Int): TbConfigure {
        this.requestMaxNum = requestMaxNum
        return this

    }

    private fun getSSLSocketFactory(context: Context, sslList: ArrayList<Int>): SSLSocketFactory {
        //CertificateFactory用来证书生成
        val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
        //Create a KeyStore containing our trusted CAs
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        //读取本地证书
        sslList.forEach {
            val input = context.resources.openRawResource(it)
            keyStore.setCertificateEntry(it.toString(), certificateFactory.generateCertificate(input))
            input.close()
        }
        //Create a TrustManager that trusts the CAs in our keyStore
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        //Create an SSLContext that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())

        return sslContext.socketFactory
    }

}

