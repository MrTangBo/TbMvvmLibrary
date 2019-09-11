package com.tb.mvvm_library.tbShareUser


import com.tb.mvvm_library.base.TbConfigure

/**
 * @author: TB
 * @package: com.tb.framelibrary.shareUser
 * @description:单例
 * @date: 2018/8/9
 * @time: 16:40
 */
class ShareUserInfoUtil private constructor() {

    /**
     * isClean表示返回可清除的SPUtils对象
     *
     * @param isClean
     * @return
     */
    companion object {
        fun getInstance(isClean: Boolean): SPUtils = if (isClean) SPUtilsHolder.spUtils_c else SPUtilsHolder.spUtils
    }

    private object SPUtilsHolder {
        val spUtils_c = SPUtils(TbConfigure.getInstance().cleanCacheName)//可清处的缓存
        val spUtils = SPUtils(TbConfigure.getInstance().cacheName)//不可清除的缓存
    }

}
