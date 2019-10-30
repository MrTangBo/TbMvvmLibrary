package com.tb.mvvm_library.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tb.mvvm_library.base.TbConfigure
import android.R.attr.resource
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.tb.mvvm_library.base.TbApplication


class GlideUtil {

    /*可以单独设置默认加载和错误图*/
    private var placeholder: Int = TbConfigure.getInstance().placeholder
    private var error: Int = TbConfigure.getInstance().error

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = GlideUtil()
    }

    /*设置加载中的图片*/
    fun setPlaceholder(resId: Int): GlideUtil {
        getInstance().placeholder = resId
        return this
    }

    /*加载错误图片*/
    fun setError(resId: Int): GlideUtil {
        getInstance().error = resId
        return this
    }


    //常规的加载
    fun showImage(
        mContext: Context,
        url: String,
        imageView: ImageView,
        scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
        isCacheImage: Boolean = true //是否缓存图片
    ) {
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(mContext)
            .load(url)
            .apply(getOptions(isCacheImage))
            .transition(DrawableTransitionOptions.withCrossFade())
            .thumbnail(0.1f)
            .into(object : DrawableImageViewTarget(imageView) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    super.onResourceReady(resource, transition)
                    imageView.scaleType = scaleType
                    imageView.setImageDrawable(resource)
                }
            })
    }

    private fun getOptions(isCacheImage: Boolean): RequestOptions {
        return RequestOptions()
            .placeholder(placeholder)//加载默认图
            .error(error)//加载失败图
            .diskCacheStrategy(if (isCacheImage) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)//缓存所有
            .priority(Priority.HIGH)//设置图片加载的优先级
            .skipMemoryCache(!isCacheImage)//是否跳过内存缓存
    }

    /*清理缓存*/
    fun glideClean() {
        Glide.get(TbApplication.mApplicationContext).clearDiskCache()
        Glide.get(TbApplication.mApplicationContext).clearMemory()
    }
}