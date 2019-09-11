package com.tb.mvvm_library.tbExtend

import androidx.recyclerview.widget.RecyclerView

/**
 *@作者：tb
 *@时间：2019/8/12
 *@描述：别名函数列表
 */
/*单击*/
typealias TbOnClick = (() -> Unit)?//点击事件别名

typealias TbOnClickInfo = ((info: Any) -> Unit)?//点击事件别名（携带参数）
/*列表点击*/
typealias TbItemClick = ((position: Int) -> Unit)?//列表item点击事件别名

typealias TbItemClickInfo = ((position: Int, info: Any) -> Unit)?//列表item点击事件别名（携带参数）
/*RecyclerView滑动*/
typealias TbOnScrolled = ((recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit)?
typealias TbOnScrollStateChanged = ((recyclerView: RecyclerView?, newState: Int) -> Unit)?

/*ViewPager滑动*/
typealias TbOnPageScrolled=((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)?
typealias TbOnPageScrollStateChanged=((state: Int) -> Unit)?
