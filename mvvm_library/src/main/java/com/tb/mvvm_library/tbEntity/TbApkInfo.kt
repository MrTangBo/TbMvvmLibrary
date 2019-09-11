package com.tb.mvvm_library.tbEntity

import java.io.Serializable

/**
 *@作者：tb
 *@时间：2019/9/2
 *@描述：Apk信息类
 */
class TbApkInfo : Serializable {
    var versionCode: Int = 1
    var versionName: String = ""
    var apkName: String = ""
    var packgeName: String = ""

}