package com.tb.mvvm_library.view

import android.content.Context
import android.widget.ImageView
import com.tb.mvvm_library.tbExtend.showImage

class DefaultImageCreator : LGNineGridView.ImageCreator {
    override fun createImageView(context: Context?): TbPressImageView {
        return TbPressImageView(context!!)
    }

    override fun loadImage(context: Context?, url: String, imageView: TbPressImageView) {
        imageView.showImage(url)
    }
    companion object {
        fun getInstance() = Holder.instance
    }
    private object Holder {
        val instance: DefaultImageCreator = DefaultImageCreator()
    }
}
