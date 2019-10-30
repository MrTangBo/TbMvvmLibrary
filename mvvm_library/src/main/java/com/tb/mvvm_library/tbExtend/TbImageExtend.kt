package com.tb.mvvm_library.tbExtend

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.databinding.TbPopSaveImageBinding
import com.tb.mvvm_library.hipermission.PermissionItem
import com.tb.mvvm_library.tbInterface.PictureZipListener
import com.tb.mvvm_library.tbZxingUtil.encode.CodeCreator
import com.tb.mvvm_library.util.GlideUtil
import com.tb.mvvm_library.view.TbPopupWindow
import com.tb.mvvm_library.tbZxingUtil.TbRGBLuminanceSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.*


/**
 *@作者：tb
 *@时间：2019/6/27
 *@描述：自定义扩展方法
 */
//常规的加载
@BindingAdapter("imageUrl", "tb_scaleType")
fun ImageView.showImage(imageUrl: String, scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP) {
    GlideUtil.getInstance().showImage(this.context, imageUrl, this, scaleType)
}

/*加载无缓存图片*/
fun ImageView.showImageNoChche(
    imageUrl: String,
    scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) {
    GlideUtil.getInstance().showImage(this.context, imageUrl, this, scaleType, false)
}

/*上传图片到自己的服务器*/
@SuppressLint("CheckResult")
fun List<String>?.tbUpLoadImage(
    name: String = "file",
    zipListener: PictureZipListener,
    key: Array<String>? = null,
    value: Array<String>? = null,
    showIndex: Boolean = false  //有的后台需要在多图片上传的时候为file0,file1,file2..
) {
    if (this.isNullOrEmpty()) return
    Flowable.just(this)
        .observeOn(Schedulers.io())
        .map {
            val fileList = arrayListOf<File>()
            it.forEach { path ->
                val file = File(path)
                if (file.exists()) {
                    fileList.add(file)
                }
            }
            fileList
        }.observeOn(AndroidSchedulers.mainThread()).subscribe {
            val partMap = HashMap<String, RequestBody>()
            it.forEachIndexed { index, file ->
                val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                partMap[name + if (showIndex) index else "" + "\"; filename=\"" + file.name] = fileBody
            }
            if (key == null || value == null) {
                zipListener.zipListener(partMap)
                return@subscribe
            }
            for (i in key.indices) {
                val valueBody = RequestBody.create(MediaType.parse("text/plain"), value[i])
                partMap[key[i]] = valueBody
            }
            zipListener.zipListener(partMap)
        }
}

//长按，通过zxing读取图片，判断是否有二维码
fun ImageView?.tbImageLongPress(
    activity: AppCompatActivity,
    readQRCode: ((readStr: String) -> Unit)? = null
) {
    if (this == null) return
    val mGestureDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            val bitmap = drawable.toBitmap()
            val pop = TbPopupWindow(activity, R.layout.tb_pop_save_image)
            val bind: TbPopSaveImageBinding = pop.popBaseBind as TbPopSaveImageBinding
            bind.saveImage.setOnClickListener {
                context.tbRequestPermission(
                    arrayListOf(
                        PermissionItem(
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "存储",
                            R.drawable.permission_ic_storage
                        )
                    ), permissionSuccess = {
                        bitmap.tbBitmapSaveSdCard("${System.currentTimeMillis()}")//保存图片
                        tbShowToast("保存成功！")
                        pop.dismiss()
                    }
                )
            }
            val re: Result? = tbReadQRCode()
            if (re == null) {
                bind.readRQCode.visibility = View.GONE
            } else {
                bind.readRQCode.visibility = View.VISIBLE
                bind.readRQCode.setOnClickListener {
                    readQRCode?.invoke(re.text)
                    pop.dismiss()
                }
            }
            pop.showAsDropDown(
                this@tbImageLongPress,
                e?.x!!.toInt(), -e.y.toInt(), Gravity.TOP
            )
        }
    })
    setOnTouchListener { _, motionEvent ->
        mGestureDetector.onTouchEvent(motionEvent)
        return@setOnTouchListener false
    }
}

/*解析二维码*/
fun ImageView?.tbReadQRCode(): Result? {
    if (this == null) return null
    val bitmap = drawable.toBitmap()
    val source = TbRGBLuminanceSource(bitmap)
    val bitmap1 = BinaryBitmap(HybridBinarizer(source))
    val hints = LinkedHashMap<DecodeHintType, Any>()
    hints[DecodeHintType.CHARACTER_SET] = "utf-8"
    val reader = MultiFormatReader()
    var re: Result? = null
    try {
        re = reader.decode(bitmap1, hints)
    } catch (e: Exception) {
    }

    return re
}

/*生成二维码*/
fun Any.tbCreateQRCode(
    contents: String,
    width: Int = tbGetDimensValue(R.dimen.x300),
    height: Int = tbGetDimensValue(R.dimen.x300),
    @IdRes logo: Int = 0
): Bitmap {
    var logoImage: Bitmap? = null
    if (logo != 0) {
        logoImage = BitmapFactory.decodeResource(TbApplication.mApplicationContext.resources, logo)
    }
    return CodeCreator.createQRCode(contents, width, height, logoImage)
}




















