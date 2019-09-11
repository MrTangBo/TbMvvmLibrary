package com.tb.test.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tb.mvvm_library.tbAdapter.BaseRecyclerAdapter
import com.tb.mvvm_library.tbExtend.init
import com.tb.mvvm_library.tbExtend.tbGetDimensValue
import com.tb.mvvm_library.tbExtend.tbShowToast
import com.tb.mvvm_library.tbExtend.tbStatusBarInit
import com.tb.mvvm_library.uiActivity.TbBaseTitleActivity
import com.tb.mvvm_library.util.LogUtils
import com.tb.test.R
import com.tb.test.databinding.ItemBinding
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : TbBaseTitleActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        tbStatusBarInit(R.color.tb_green)
        rootLayoutId = R.layout.activity_main3
        super.onCreate(savedInstanceState)
    }


    override fun initView() {
        super.initView()
        initToolBar()
        initMenu(arrayListOf("搜索"),menuClick = {position, view ->

        })
        mSearchView?.init(onQuerySubmit = {str ->
            tbShowToast(str)
        })
        val listData = arrayListOf<String>()
        for (i in 0..20) {
            listData.add("测试数据---》$i")
        }
        list.init(
            listData,
            TestAdapter(listData, R.layout.item),
            itemClick = { position ->
                tbShowToast("$position")
            },
            scrollYListener = {scrollY, isTopDirection ->
                LogUtils.log("tb----y$scrollY")
            },
            dividerSize = tbGetDimensValue(R.dimen.x20)
        )

    }


    class TestAdapter(
        listData: ArrayList<*>, @LayoutRes layoutId: Int
    ) : BaseRecyclerAdapter(listData, layoutId) {
        override fun onBind(holder: MyHolder, position: Int) {
            when (holder.itemBinding) {
                is ItemBinding -> {
                    val itemBind: ItemBinding = holder.itemBinding as ItemBinding
                    itemBind.str = listData[position] as String
                    itemBind.root.setOnClickListener {
                        tbItemClick?.invoke(position)
                    }
                }
            }

        }
    }
}