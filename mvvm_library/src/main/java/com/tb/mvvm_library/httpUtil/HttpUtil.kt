package com.tb.mvvm_library.httpUtil

import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.tbExtend.tb2Json
import com.tb.mvvm_library.util.GsonUtil
import com.tb.mvvm_library.util.LogUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.function.Function

/**
 *@作者：tb
 *@时间：2019/6/28
 *@描述：网络请求工具类
 */
class HttpUtil {

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = HttpUtil()
    }

    /*请求*/
    fun startRequest(flowables: ArrayList<Flowable<*>>, requestListener: RequestListener, taskId: Int) {
        var retryCount = TbConfigure.getInstance().requestMaxNum//当前重连次数
        Flowable.concat(flowables)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retryWhen {
                return@retryWhen it.flatMap { throwable ->
                    if (throwable is IOException) {
                        if (++retryCount <= TbConfigure.getInstance().maxRetries) {
                            LogUtils.log("重连第$retryCount 次")
                            Flowable.timer(TbConfigure.getInstance().retryDelayMillis, TimeUnit.SECONDS)
                        } else {
                            Flowable.error(throwable)
                        }
                    } else {
                        Flowable.error(throwable)
                    }
                }
            }.subscribe(object : Subscriber<Any> {
                override fun onComplete() {
                    requestListener.onComplete(taskId)
                }

                override fun onSubscribe(s: Subscription?) {
                    s?.request(TbConfigure.getInstance().requestMaxNum.toLong())
                    requestListener.start(s, taskId)
                }

                override fun onNext(t: Any) {
                    requestListener.onNext(t, taskId)
                }

                override fun onError(t: Throwable?) {
                    requestListener.onError(t, taskId)
                }
            })

    }

}






