package com.tb.mvvm_library.tbDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import com.tb.design.library.tbUtil.FontUtil
import com.tb.mvvm_library.base.TbConfigure
import kotlinx.android.synthetic.main.dialog_loading.view.*

/**
 *@作者：tb
 *@时间：2019/7/23
 *@描述：加载进度
 */
open class TbLoadingDialog(
    var mConext: Context,
    var remark: String = "加载中...",
    @LayoutRes var layoutId: Int = TbConfigure.getInstance().loadingLayoutId,
    @StyleRes styleId: Int = TbConfigure.getInstance().loadingStyleId
) : Dialog(mConext, styleId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDialog = LayoutInflater.from(mConext).inflate(layoutId, null)
        FontUtil.replaceFont(viewDialog, TbConfigure.getInstance().fontType)
        viewDialog.loadingText.text = remark
        setContentView(viewDialog)
        val windowDia = window
        val lp = windowDia!!.attributes
        lp.gravity = Gravity.CENTER
        windowDia.attributes = lp
        setCanceledOnTouchOutside(false)
    }

}