package com.tb.test.activity

import android.os.Bundle
import com.tb.mvvm_library.tbAdapter.PageFragmentAdapter
import com.tb.mvvm_library.tbExtend.tbGetPhoneOnlyNum
import com.tb.mvvm_library.tbExtend.tbGetPhoneSize
import com.tb.mvvm_library.tbExtend.tbStatusBarInit
import com.tb.mvvm_library.uiActivity.TbBaseActivity
import com.tb.mvvm_library.uiActivity.TbBaseTitleActivity
import com.tb.test.R
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : TbBaseTitleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        tbStatusBarInit(R.color.colorAccent)
        rootLayoutId = R.layout.activity_navigation
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        tbGetPhoneOnlyNum()

        initToolBar(bgColor = R.color.tb_green)
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
                R.drawable.def_qq,
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