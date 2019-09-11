package com.tb.test.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tb.mvvm_library.uiFragment.TbBaseFragment
import com.tb.mvvm_library.uiFragment.TbBaseTitleFragment
import com.tb.test.R
import kotlinx.android.synthetic.main.header.*

class TestFragment : TbBaseFragment() {
    override fun loadData() {
        scrollTx.text = "士大夫士大夫了收到了烦死了都疯了似的发，发生的封建士大夫精神的；发士大夫"

        verticalScrollTx.setDataSource(arrayListOf("asdasdasdasd","萨科技大厦的","sajkdsajd"))
        verticalScrollTx.setSleepTime(2000)
        verticalScrollTx.setAnimDuration(300)
        verticalScrollTx.startPlay()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootLayoutId = R.layout.header
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}