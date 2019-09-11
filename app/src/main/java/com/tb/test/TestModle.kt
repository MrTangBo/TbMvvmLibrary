package com.tb.test

import com.tb.mvvm_library.base.TbEventBusInfo
import com.tb.mvvm_library.httpUtil.RetrofitApi
import com.tb.mvvm_library.model.TbBaseModel
import com.tb.test.activity.HttpResultInfo
import com.tb.test.activity.TestInfo
import io.reactivex.Flowable


class TestModle : TbBaseModel() {
    override fun <T> onNext(t: T, taskId: Int) {
        super.onNext(t, taskId)
        val result = t as HttpResultInfo<*>
        /*这里添加统一处理Code*/
        if (result.reason == "successed") {
            val list: ArrayList<Any> = arrayListOf()
            list.add(result.result as Any)
            list.add(taskId)
            liveData.postValue(list)
        }
    }

    override fun getData() {
        requestParams["postcode"] = "215001"
        requestParams["key"] = "738d664a58611411802689a5c11f1d9a"
        requestParams["page"] = "1"
        requestParams["pagesize"] = "20"
        var ss: Flowable<*> =
            RetrofitApi.getInstance().getInterface(RetrofitInterface::class.java).getData(requestParams)
        startRequest(ss,0)
    }

    override fun onUserEvent(event: TbEventBusInfo) {
        super.onUserEvent(event)
        liveDataList.forEach {
            val vv = it.value as HttpResultInfo<*>
            val v = vv.result
            when (v) {
                is TestInfo -> v.currentpage = 1000
            }
        }
    }
}