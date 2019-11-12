package com.tb.mvvm_library.tbDialog

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tb.mvvm_library.R
import com.tb.mvvm_library.databinding.TbSureDialogBinding
import com.tb.mvvm_library.tbExtend.TbOnClick
import com.tb.mvvm_library.tbExtend.tbGetDimensValue

/**
 *@作者：tb
 *@时间：2019/7/26
 *@描述：需要确认操作再次提醒
 */
open class TbSureDialog(
    var titleTx: String = "",
    var messageTx: String = "",
    var sureTx: String = "确定",
    var cancelTx: String = "取消"
) : TbBaseDialog() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutId(R.layout.tb_sure_dialog)
        setWidth(tbGetDimensValue(R.dimen.x600))
    }

    var sureClick: TbOnClick = null
    var cancelClick: TbOnClick = null

    var binding: TbSureDialogBinding? = null

    var setView: ((binding: TbSureDialogBinding) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = dialogBing as TbSureDialogBinding
        binding?.sure?.setOnClickListener {
            sureClick?.invoke()
            dismiss()
        }
        binding?.cancel?.setOnClickListener {
            cancelClick?.invoke()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.titleTx = titleTx
        binding?.messageTx = messageTx
        binding?.sureTx = sureTx
        binding?.cancelTx = cancelTx

        binding?.let {
            setView?.invoke(it)
        }
    }

}