package com.tb.mvvm_library.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.tb.mvvm_library.R
import com.tb.mvvm_library.tbExtend.tbDip2px
import com.tb.mvvm_library.tbExtend.tbShowToast
import com.tb.mvvm_library.tbExtend.tbSp2px
import kotlinx.android.synthetic.main.view_car_count.view.*

/**
 *@作者：tb
 *@时间：2019/11/27
 *@描述：购物车数量加减空间
 */
class CarCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mAddIcon: Int = R.drawable.icon_car_add
    private var mReduceIcon: Int = R.drawable.icon_car_reduce
    private var mTextColor: Int = 0x333333
    private var mTextSize = 13
    private var mIconWidth: Int = 40
    private var mEditWidth: Int = 35
    private var mBg: Int = R.drawable.bg_cat_count_view
    var stockNum: Int = 9990//库存

    var numChangeListener: ((num: Int) -> Unit)? = null

    private val layoutView: View = LayoutInflater.from(context).inflate(R.layout.view_car_count, this, false)


    init {
        initData(attrs)
        initView()
        addView(layoutView)
    }


    /*初始化属性*/
    private fun initData(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CarCountView)
        mAddIcon = typeArray.getResourceId(R.styleable.CarCountView_addIcon, mAddIcon)
        mReduceIcon = typeArray.getResourceId(R.styleable.CarCountView_reduceIcon, mReduceIcon)
        mTextColor = typeArray.getColor(R.styleable.CarCountView_textColor, resources.getColor(R.color.tb_text_black))
        mTextSize =
            typeArray.getDimensionPixelSize(R.styleable.CarCountView_textSize, tbSp2px(mTextSize.toFloat()))
        mIconWidth =
            typeArray.getDimensionPixelSize(R.styleable.CarCountView_iconWidth, tbDip2px(mIconWidth.toFloat()))
        mEditWidth =
            typeArray.getDimensionPixelSize(R.styleable.CarCountView_editWidth, tbDip2px(mEditWidth.toFloat()))
        mBg = typeArray.getResourceId(R.styleable.CarCountView_rootBg, mBg)
        stockNum = typeArray.getInteger(R.styleable.CarCountView_stockNum, stockNum)
        typeArray.recycle()
        layoutView.carCountAdd.setImageResource(mAddIcon)
        layoutView.carCountReduce.setImageResource(mReduceIcon)
        layoutView.carCountNum.setTextColor(mTextColor)
        layoutView.carCountNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        layoutView.rootView.background = ContextCompat.getDrawable(context, mBg)
        LinearLayout.LayoutParams(mIconWidth, LinearLayout.LayoutParams.MATCH_PARENT).let {
            layoutView.carCountReduce.layoutParams = it
            layoutView.carCountAdd.layoutParams = it
        }
        LinearLayout.LayoutParams(mEditWidth, LinearLayout.LayoutParams.MATCH_PARENT).let {
            layoutView.carCountNum.layoutParams = it
        }
    }

    fun setEditNum(num: String) {
        layoutView.carCountNum.setText(num)
    }

    /*  初始化点击事件*/
    private fun initView() {
        layoutView.carCountAdd.setOnClickListener {
            if (layoutView.carCountNum.text.isNullOrEmpty()) {
                layoutView.carCountNum.setText("1")
                layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
            }
            if (layoutView.carCountNum.text.toString().toInt() >= stockNum) {
                tbShowToast("不能超过最大库存$stockNum！")
            } else {
                layoutView.carCountNum.setText((layoutView.carCountNum.text.toString().toInt() + 1).toString())
                layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
            }
        }
        layoutView.carCountReduce.setOnClickListener {
            if (layoutView.carCountNum.text.isNullOrEmpty()) {
                layoutView.carCountNum.setText("1")
                layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
            }
            if (layoutView.carCountNum.text.toString().toInt() > 1) {
                layoutView.carCountNum.setText((layoutView.carCountNum.text.toString().toInt() - 1).toString())
                layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
            }
        }

        layoutView.carCountNum.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                return@doAfterTextChanged
            }
            when {
                it.toString() == "0" -> {
                    layoutView.carCountNum.setText("1")
                    layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
                    tbShowToast("商品个数不能小于0")
                    numChangeListener?.invoke(layoutView.carCountNum.text.toString().toInt())
                }
                it.toString().toInt() > stockNum -> {
                    layoutView.carCountNum.setText(stockNum.toString())
                    layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
                    tbShowToast("不能超过最大库存$stockNum！")
                    numChangeListener?.invoke(layoutView.carCountNum.text.toString().toInt())
                }
                else -> numChangeListener?.invoke(layoutView.carCountNum.text.toString().toInt())
            }

        }
        layoutView.carCountNum.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (layoutView.carCountNum.text.isNullOrEmpty()) {
                    layoutView.carCountNum.setText("1")
                    layoutView.carCountNum.setSelection(layoutView.carCountNum.text.toString().length)
                }
            }

        }
    }
}