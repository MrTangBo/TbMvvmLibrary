package com.tb.mvvm_library.httpUtil

import org.reactivestreams.Subscription

interface RequestListener {
    fun start(s: Subscription?,taskId:Int)
    fun <T> onNext(t: T,taskId:Int)
    fun onComplete(taskId:Int)
    fun onError(t: Throwable?,taskId:Int)

}