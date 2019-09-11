package com.tb.test

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.RelativeLayout
import androidx.customview.widget.ViewDragHelper
import com.tb.mvvm_library.tbExtend.tbGetDimensValue
import com.tb.mvvm_library.tbExtend.tbStatusBarHeight


class DragHelperRelative @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mDragHelper: ViewDragHelper
    private var mCallBack: MyHelperDragCallBack

    init {
        mCallBack = MyHelperDragCallBack()
        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallBack)
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }


    inner class MyHelperDragCallBack : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return mWebView == child
        }


        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (top < tbGetDimensValue(R.dimen.x98) + tbStatusBarHeight()[0]) {
                return tbGetDimensValue(R.dimen.x98) + tbStatusBarHeight()[0]
            }
            if (top > measuredHeight - tbGetDimensValue(R.dimen.x98)) {
                return measuredHeight - tbGetDimensValue(R.dimen.x98)
            }

            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (releasedChild.top < measuredHeight / 2) {
                mDragHelper.smoothSlideViewTo(
                    releasedChild,
                    releasedChild.left,
                    tbGetDimensValue(R.dimen.x98) + tbStatusBarHeight()[0]
                )
                postInvalidate()
            } else {
                mDragHelper.smoothSlideViewTo(
                    releasedChild,
                    releasedChild.left,
                    measuredHeight - tbGetDimensValue(R.dimen.x98)
                )
                postInvalidate()
            }
        }


        override fun getViewVerticalDragRange(child: View): Int {
            return child.measuredHeight
        }


        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            mLastX = changedView.x
            mLastY = changedView.y


        }
    }

    // 记录最后的位置
    var mLastX = -1f
    var mLastY = -1f
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (mLastY != -1f) {
            mWebView.layout(
                mLastX.toInt(),
                mLastY.toInt(),
                mLastX.toInt() + mWebView.measuredWidth,
                mLastY.toInt() + mWebView.measuredHeight
            )
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (mWebView.scrollY == 0) {
            mDragHelper.shouldInterceptTouchEvent(ev!!)
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDragHelper.processTouchEvent(event!!)
        return true
    }

    override fun computeScroll() {
        //固定写法
        //此方法用于自动滚动,比如自动回滚到默认位置.
        if (mDragHelper.continueSettling(true)) {
            postInvalidate()
        }
    }


    lateinit var mWebView: WebView

    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        mWebView = findViewById(R.id.mWebView)
    }
}