package com.tb.mvvm_library.tbDialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.tb.mvvm_library.R
import com.tb.mvvm_library.databinding.TbSelectPictureDialogBinding
import com.tb.mvvm_library.tbExtend.TbOnClick

/**
 *@作者：tb
 *@时间：2019/8/16
 *@描述：选择图库工具类
 */
open class TbSelectPictureDialog : TbBaseDialog() {

    var binding: TbSelectPictureDialogBinding? = null
    var pictureClick: TbOnClick = null
    var takePhotoClick: TbOnClick = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setMyStyle(R.style.tbSelectPictureStyle)
        super.onCreate(savedInstanceState)
        setLayoutId(R.layout.tb_select_picture_dialog)
        setGravity(Gravity.BOTTOM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = dialogBing as TbSelectPictureDialogBinding
        binding?.openGallery?.setOnClickListener {
            pictureClick?.invoke()
            dismiss()
        }
        binding?.openTakePhoto?.setOnClickListener {
            takePhotoClick?.invoke()
            dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dismiss()
        }
    }
}