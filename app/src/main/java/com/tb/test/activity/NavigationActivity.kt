package com.tb.test.activity

import android.os.Bundle
import com.tb.mvvm_library.tbAdapter.PageFragmentAdapter
import com.tb.mvvm_library.uiActivity.TbBaseActivity
import com.tb.test.R
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : TbBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        rootLayoutId = R.layout.activity_navigation
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        mViewPager.adapter = PageFragmentAdapter(
            supportFragmentManager,
            arrayListOf(TestFragment(), TestFragment(), TestFragment(), TestFragment(), TestFragment())
        )
        bottomNavigation.setTitle(
            arrayListOf("首页", "论坛", "订单", "消息", "我的"),
            arrayListOf(
                R.drawable.icon_close,
                R.drawable.icon_close,
                R.drawable.icon_close,
                R.drawable.icon_close,
                R.drawable.icon_close
            ),
            arrayListOf(
                R.drawable.ic_delete_photo,
                R.drawable.ic_delete_photo,
                R.drawable.ic_delete_photo,
                R.drawable.ic_delete_photo,
                R.drawable.ic_delete_photo
            )
            ,
            mViewPager = mViewPager
        )
            .setBadgeNumSingle(3, 20, bgColor = R.color.colorAccent, moveUpListener = { badge, targetView ->
            })
    }
}