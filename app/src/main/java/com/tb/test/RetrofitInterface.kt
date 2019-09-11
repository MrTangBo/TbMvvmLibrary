package com.tb.test

import com.tb.test.activity.HttpResultInfo
import com.tb.test.activity.TestInfo
import io.reactivex.Flowable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {

    @FormUrlEncoded
    @POST("/postcode/query?")
    fun getData(@FieldMap map: MutableMap<String, String>): Flowable<HttpResultInfo<TestInfo>>


    @FormUrlEncoded
    @POST("/aosuite/notokenapi/app/v1/queryPrintCode.jhtml")
    fun getData01(@FieldMap map: MutableMap<String, String>): Flowable<HttpResultInfo<String>>
}