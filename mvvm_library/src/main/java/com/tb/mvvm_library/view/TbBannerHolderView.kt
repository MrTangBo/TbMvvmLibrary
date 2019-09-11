package com.tb.mvvm_library.view

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bigkoo.convenientbanner.holder.Holder
import com.makeramen.roundedimageview.RoundedImageView
import com.tb.mvvm_library.R
import com.tb.mvvm_library.tbExtend.showImage
import com.tb.mvvm_library.tbExtend.tbGetDimensValue
import com.tb.mvvm_library.util.LogUtils
import kotlinx.android.synthetic.main.tb_item_banner.view.*

/**
 *@作者：tb
 *@时间：2019/8/8
 *@描述：banner item设置
 */
open class TbBannerHolderView<T>(
    itemView: View?,
    private var circleSizeRect: Rect,
    private var marginRect: Rect,
    var scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) : Holder<T>(itemView) {
    private var imageView: RoundedImageView? = null
    override fun updateUI(data: T) {
        val layoutParams: LinearLayout.LayoutParams = imageView?.layoutParams as LinearLayout.LayoutParams
        layoutParams.setMargins(marginRect.left, marginRect.top, marginRect.right, marginRect.bottom)
        imageView?.layoutParams = layoutParams
        imageView?.setCornerRadius(
            circleSizeRect.left.toFloat(),
            circleSizeRect.top.toFloat(),
            circleSizeRect.right.toFloat(),
            circleSizeRect.bottom.toFloat()
        )
        if (data is String) {
            imageView?.showImage(data, scaleType)
        } else if (data is Int) {
            imageView?.scaleType = scaleType
            imageView?.setImageResource(data)
        }
    }

    override fun initView(itemView: View?) {
        imageView = itemView!!.bannerImage
    }

}