package com.tb.mvvm_library.tbAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by Administrator on 2016/6/28 0028.
 */
class PageFragmentAdapter : FragmentPagerAdapter {


    private var fragmentList: List<Fragment>? = null
    private var titles: Array<String>? = null
    private var fm: FragmentManager? = null

    constructor(fm: FragmentManager, fragmentList: List<Fragment>) : super(fm) {
        this.fragmentList = fragmentList
        this.fm = fm
    }

    constructor(fm: FragmentManager, fragmentList: List<Fragment>, titles: Array<String>) : super(fm) {
        this.fragmentList = fragmentList
        this.fm = fm
        this.titles = titles
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles != null) {
            titles!![position]
        } else super.getPageTitle(position)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE  //没有找到child要求重新加载
    }
}
