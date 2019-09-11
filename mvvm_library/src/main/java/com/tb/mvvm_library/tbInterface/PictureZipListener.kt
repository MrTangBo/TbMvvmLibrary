package com.tb.mvvm_library.tbInterface

import okhttp3.RequestBody

/**
 *@作者：tb
 *@时间：2019/7/3
 *@描述：压缩图片成功的回调
 */
interface PictureZipListener {
    fun zipListener(partMap: MutableMap<String, RequestBody>)
}
