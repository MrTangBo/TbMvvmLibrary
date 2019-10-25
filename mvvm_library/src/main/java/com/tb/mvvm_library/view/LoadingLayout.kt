package com.tb.mvvm_library.view

import android.content.Context
import android.content.IntentFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.flyco.roundview.RoundFrameLayout
import com.flyco.roundview.RoundTextView
import com.flyco.roundview.RoundViewDelegate
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbConfigure

/**
 *@作者：tb
 *@时间：2019/7/10
 *@描述：
 */

class LoadingLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RoundFrameLayout(context, attrs) {

    var mEmptyView: View? = null
    var mErrorView: View? = null
    var mLoadingView: View? = null
    var errorClick: ((view: View) -> Unit)? = null
    var emptyClick: ((view: View) -> Unit)? = null
    private var mLayoutInflater: LayoutInflater? = null

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, 0, 0)
        try {
            val emptyView = a.getResourceId(R.styleable.LoadingLayout_emptyView, R.layout.tb_include_empty)
            val errorView = a.getResourceId(R.styleable.LoadingLayout_errorView, R.layout.tb_include_error)
            val loadingView = a.getResourceId(R.styleable.LoadingLayout_loadingView, R.layout.tb_include_loading)

            mLayoutInflater = LayoutInflater.from(getContext())
            mEmptyView = mLayoutInflater!!.inflate(emptyView, this, true)
            mErrorView = mLayoutInflater!!.inflate(errorView, this, true)
            mLoadingView = mLayoutInflater!!.inflate(loadingView, this, true)
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }

        findViewById<RoundTextView>(R.id.emptyText).setOnClickListener {
            emptyClick?.invoke(it)
        }

        findViewById<RoundTextView>(R.id.errorText).setOnClickListener {
            errorClick?.invoke(it)
        }
    }

    fun setErrorIcon(error:Int){
        findViewById<AppCompatImageView>(R.id.errorIcon).setImageResource(error)
    }

    fun setUi(
        emptyImgId: Int = TbConfigure.getInstance().emptyIcon,
        errorImgId: Int = TbConfigure.getInstance().errorIcon,
        emptyDescribe: CharSequence = "",
        errorDescribe: CharSequence = "",
        refreshEmpty: CharSequence = "",
        refreshError: CharSequence = "",
        delegate: ((text: RoundTextView) -> Unit)? = null
    ) {
        if (refreshEmpty.isEmpty()) {
            findViewById<RoundTextView>(R.id.emptyText).visibility = View.GONE
        } else {
            delegate?.invoke(findViewById(R.id.emptyText))
        }
        if (refreshError.isEmpty()) {
            findViewById<RoundTextView>(R.id.errorText).visibility = View.GONE
        } else {
            delegate?.invoke(findViewById(R.id.errorText))
        }
        findViewById<TextView>(R.id.emptyDescribe).text = emptyDescribe
        findViewById<RoundTextView>(R.id.emptyText).text = refreshEmpty
        findViewById<TextView>(R.id.errorDescribe).text = errorDescribe
        findViewById<RoundTextView>(R.id.errorText).text = refreshError
        findViewById<AppCompatImageView>(R.id.emptyIcon).setImageResource(emptyImgId)
        findViewById<AppCompatImageView>(R.id.errorIcon).setImageResource(errorImgId)
    }

    fun showEmpty() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == 0) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    fun showError() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == 1) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    fun showLoading() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == 2) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    fun showContent() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == 3) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    fun setEmptyView(@LayoutRes layout: Int): LoadingLayout {
        removeView(getChildAt(0))
        mEmptyView = mLayoutInflater!!.inflate(layout, null, true)
        addView(mEmptyView, 0)
        onFinishInflate()
        return this
    }

    fun setErrorView(@LayoutRes layout: Int): LoadingLayout {
        removeView(getChildAt(1))
        mErrorView = mLayoutInflater!!.inflate(layout, null, true)
        addView(mErrorView, 1)
        onFinishInflate()
        return this
    }

    fun setLoadingView(@LayoutRes layout: Int): LoadingLayout {
        removeView(getChildAt(2))
        mLoadingView = mLayoutInflater!!.inflate(layout, null, true)
        addView(mLoadingView, 2)
        return this
    }

}
