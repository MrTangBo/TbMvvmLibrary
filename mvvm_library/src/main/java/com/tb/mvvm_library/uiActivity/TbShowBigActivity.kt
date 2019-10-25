package com.tb.mvvm_library.uiActivity

import android.os.Bundle
import com.tb.mvvm_library.R
import com.tb.mvvm_library.tbAdapter.BasePagerAdapter
import com.tb.mvvm_library.tbExtend.tbOnPageLisener
import com.tb.mvvm_library.tbExtend.tbStatusBarInit
import com.tb.mvvm_library.uiActivity.TbBaseTitleActivity
import com.tb.mvvm_library.util.SpanUtils
import kotlinx.android.synthetic.main.activity_show_big.*

class TbShowBigActivity : TbBaseTitleActivity() {

    private lateinit var imgUrls: List<*>
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        tbStatusBarInit(statusColorId = R.color.tb_black)
        rootLayoutId = R.layout.activity_show_big
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        initToolBar(bgColor = R.color.tb_black)
        imgUrls = intent.getSerializableExtra("imgUrls") as List<*>
        if (imgUrls.isEmpty()) return
        viewPager.pageMargin = (resources.displayMetrics.density * 15).toInt()
        currentPosition=intent.getIntExtra("position", 0)
        setTitleCenter(title = "${currentPosition + 1}/${imgUrls.size}", color = R.color.tb_white)
        val pagerAdapter = BasePagerAdapter(imgUrls, this)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = currentPosition
        SpanUtils.with(mCenterTextView).append("${currentPosition+1}/").append("${imgUrls.size}").create()
        viewPager.tbOnPageLisener(onPageSelected = {position ->
            SpanUtils.with(mCenterTextView).append("${position + 1}/").append("${imgUrls.size}").create()
        })
    }

}
