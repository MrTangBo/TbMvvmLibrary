package com.tb.mvvm_library.model

import android.app.Activity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.gson.JsonSyntaxException
import com.liaoinstan.springview.widget.SpringView
import com.tb.mvvm_library.base.TbEventBusInfo
import com.tb.mvvm_library.httpUtil.HttpUtil
import com.tb.mvvm_library.httpUtil.RequestListener
import com.tb.mvvm_library.tbExtend.*
import com.tb.mvvm_library.tbInterface.LoadDialogListener
import com.tb.mvvm_library.tbReceiver.TbBaseReceiver
import com.tb.mvvm_library.uiActivity.TbBaseActivity
import com.tb.mvvm_library.uiFragment.TbBaseFragment
import com.tb.mvvm_library.util.LogUtils
import io.reactivex.Flowable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.reactivestreams.Subscription
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 *@作者：TB
 *@时间：2019/6/26
 *@描述：model基类
 */
abstract class TbBaseModel : ViewModel(), LifecycleObserver, RequestListener {

    var requestParams: MutableMap<String, String> = mutableMapOf()
    var requestParamsList: ArrayList<MutableMap<String, String>> = arrayListOf()
    var liveDataList: MutableMap<Int, Any> = mutableMapOf()
    var liveData: MutableLiveData<Any> = MutableLiveData()
    var lodDialogListener: LoadDialogListener? = null
    var isShowLoadDialog: Boolean = true

    private var subscription: Subscription? = null

    var mActivity: Activity? = null
    var mFragment: Fragment? = null

    lateinit var eventBundle: Bundle

    var page: Int = 1
    var mSpringView: SpringView? = null

    var currentTaskId = -1

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onAny() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        EventBus.getDefault().register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        subscription?.cancel()
        isShowLoadDialog = false
        lodDialogListener = null
        liveDataList.clear()
        EventBus.getDefault().unregister(this)
    }

    /*eventBus回调*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onUserEvent(event: TbEventBusInfo) {
        eventBundle = event.bundle
        when (eventBundle.getString("flag")) {
            TbBaseReceiver::class.java.simpleName -> {
                if (liveDataList.isEmpty()) {
                    getData()
                }
            }
        }
    }

    /*获取数据*/
    abstract fun getData()

    /*开始请求*/
    open fun startRequest(
        flowable: Flowable<*>? = null,
        taskId: Int,
        flowables: ArrayList<Flowable<*>> = arrayListOf()
    ) {
        currentTaskId = taskId
        if (flowable != null && flowables.isEmpty()) {
            flowables.add(flowable)
            requestParamsList.add(requestParams)
            LogUtils.log("commitData--->${requestParams.tb2Json()}")
        } else {
            LogUtils.log("commitData--->${requestParamsList.tb2Json()}")
        }
        if (tbNetWorkIsConnect()) {
            if (isShowLoadDialog) {
                lodDialogListener?.showLoadDialog()
            }
            HttpUtil.getInstance().startRequest(flowables, this, taskId)
        } else {
            onComplete(taskId)
            when {
                tbNetWorkIsWifi() -> {
                    tbShowToast("当前wifi网络不可用！")
                    TbBaseReceiver.isFirst = false
                    return
                }
                tbNetWorkIsMobile() -> {
                    TbBaseReceiver.isFirst = false
                    tbShowToast("当前移动网络不可用！")
                    return
                }
                else -> {
                    TbBaseReceiver.isFirst = false
                    tbShowToast("当前网络不可用！")
                    return
                }
            }
        }
    }

    override fun start(s: Subscription?, taskId: Int) {
        subscription = s
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> onNext(t: T, taskId: Int) {
        liveDataList[taskId] = t as Any
    }

    override fun onComplete(taskId: Int) {
        dismissDialog(true)
    }

    override fun onError(t: Throwable?, taskId: Int) {
        checkThrowable(t)
        dismissDialog(false)
    }

    private fun checkThrowable(t: Throwable?) {
        when (t) {
            is ConnectException, is UnknownHostException -> {
                tbShowToast("链接出错！")
            }
            is TimeoutException, is SocketTimeoutException -> {
                tbShowToast("链接超时！")
            }
            is JsonSyntaxException -> {
                tbShowToast("数据解析错误！")
            }
            else -> tbShowToast(t.tb2Json())
        }
    }


    /*取消加载进度框*/
    open fun dismissDialog(isComplete: Boolean) {
        isShowLoadDialog = true
        lodDialogListener?.dismissLoadDialog()
        mSpringView?.onFinishFreshAndLoad()
        mActivity?.let {
            it as TbBaseActivity
            if (isComplete) {
                it.loadLayout?.showContent()
            } else {
                it.loadLayout?.showError()
            }
            it.loadingDialog?.dismiss()
        }
        mFragment?.let {
            it as TbBaseFragment
            if (isComplete) {
                it.loadLayout?.showContent()
            } else {
                it.loadLayout?.showError()
            }
            it.loadingDialog?.dismiss()
        }
    }

    //下拉刷新
    open fun tbOnRefresh() {
        page = 1
        isShowLoadDialog = false
        getData()
    }

    /*上啦加载*/
    open fun tbOnLoadmore() {
        page++
        isShowLoadDialog = false
        getData()
    }

}
