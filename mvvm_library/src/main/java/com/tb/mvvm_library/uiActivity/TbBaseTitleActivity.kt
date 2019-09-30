package com.tb.mvvm_library.uiActivity

import android.graphics.Rect
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.tbExtend.TbOnClick
import com.tb.mvvm_library.tbExtend.tbGetDimensValue
import kotlinx.android.synthetic.main.tb_include_toolbar.*


open class TbBaseTitleActivity : TbBaseActivity() {

    open lateinit var mCenterTextView: TextView
    open lateinit var mLeftTextView: TextView
    open lateinit var mToolbar: FrameLayout
    open var mSearchView: SearchView? = null
    open lateinit var mBackIcon: AppCompatImageView
    open lateinit var mRightLinear: LinearLayout


    override fun init() {
        super.init()
        mCenterTextView = tb_toolbar_center_text
        mLeftTextView = tb_toolbar_left_text
        mToolbar = tb_toolbar_f
        mBackIcon = backIcon
        mSearchView = tb_toolbar_searView
        mRightLinear = right_Linear
    }

    open fun initToolBar(
        iconRect: Rect = Rect(
            tbGetDimensValue(R.dimen.x25),
            tbGetDimensValue(R.dimen.x25),
            tbGetDimensValue(R.dimen.x25),
            tbGetDimensValue(R.dimen.x25)
        ),
        navigationIcon: Int = R.drawable.tb_back_white,
        bgColor: Int = R.color.tb_green,
        bgColorArgb: Int = -1,
        paddingTop: Int = 0
    ) {
        backIcon.setPadding(iconRect.left, iconRect.top, iconRect.right, iconRect.bottom)
        backIcon.setImageDrawable(
            if (navigationIcon == 0) null else ContextCompat.getDrawable(
                mContext,
                navigationIcon
            )
        )
        mToolbar.setPadding(0, paddingTop, 0, 0)
        if (bgColorArgb != -1) {
            mToolbar.setBackgroundColor(bgColorArgb)
        } else {
            mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, bgColor))
        }
        mBackIcon.setOnClickListener {
            clickBackIcon()
        }
    }


    /*设置左边Title*/
    open fun setTitleLeft(
        title: String?,
        color: Int = R.color.tb_white,
        size: Int = R.dimen.tb_text28,
        style: Int = Typeface.NORMAL,
        click: TbOnClick = null

    ) {
        mLeftTextView.text = title
        mLeftTextView.setTextColor(ContextCompat.getColor(mContext, color))
        mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tbGetDimensValue(size).toFloat())
        mLeftTextView.typeface = Typeface.defaultFromStyle(style)
        mLeftTextView.setOnClickListener {
            click?.invoke()
        }
    }


    /*设置中间Title*/
    open fun setTitleCenter(
        title: String?,
        color: Int = R.color.tb_white,
        size: Int = R.dimen.toolbarCenterTextSize,
        style: Int = Typeface.NORMAL
    ) {

        mCenterTextView.text = title
        mCenterTextView.setTextColor(ContextCompat.getColor(mContext, color))
        mCenterTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tbGetDimensValue(size).toFloat())
        mCenterTextView.typeface = Typeface.defaultFromStyle(style)
    }

    open fun initMenu(
        menuTitles: ArrayList<*>,
        menuClick: ((position: Int, view: View) -> Unit)? = null,
        titleColor: Int = R.color.white,
        titleSize: Int = tbGetDimensValue(R.dimen.tb_text26),
        clickRipple: Int = R.drawable.bg_tb_ripple
    ) {
        val paramas: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, tbGetDimensValue(R.dimen.x70)
            )
        menuTitles.forEachIndexed { index, it ->
            when (it) {
                is String -> {
                    val text = TextView(this)
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
                    text.setTextColor(ContextCompat.getColor(this, titleColor))
                    text.background = ContextCompat.getDrawable(TbApplication.mApplicationContext, clickRipple)
                    text.text = it
                    text.gravity = Gravity.CENTER
                    text.minWidth = tbGetDimensValue(R.dimen.x88)
                    paramas.gravity = Gravity.CENTER
                    text.layoutParams = paramas
                    mRightLinear.addView(text)
                    text.setOnClickListener { menuClick?.invoke(index, text) }
                }

                is Int -> {
                    val image = AppCompatImageView(this)
                    image.background = ContextCompat.getDrawable(TbApplication.mApplicationContext, clickRipple)
                    image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    image.setImageResource(it)
                    paramas.width = tbGetDimensValue(R.dimen.x88)
                    paramas.gravity = Gravity.CENTER
                    image.layoutParams = paramas
                    mRightLinear.addView(image)
                    image.setOnClickListener { menuClick?.invoke(index, image) }
                }

            }

        }
    }

    /*点击返回键*/
    open fun clickBackIcon() {
        if (mSearchView != null) {
            val mSearchAutoComplete: SearchView.SearchAutoComplete? = mSearchView?.findViewById(R.id.search_src_text)
            if (mSearchAutoComplete!!.isShown && mSearchView?.isIconfiedByDefault == true) {
                mSearchAutoComplete.text.clear()
                val method = mSearchView?.javaClass?.getDeclaredMethod("onCloseClicked")
                method?.isAccessible = true
                method?.invoke(mSearchView)
            } else {
                finishAfterTransition()
            }
        } else {
            finishAfterTransition()
        }
    }
}