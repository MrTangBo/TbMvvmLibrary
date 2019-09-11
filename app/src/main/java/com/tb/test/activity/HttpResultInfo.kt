package com.tb.test.activity

/**
 *@作者：tb
 *@时间：2019/6/28
 *@描述：请求成功的处理类
 */
class HttpResultInfo<T> {
    var reason: String? = null
    var result: T? = null
    var error_code: Int? = null
}