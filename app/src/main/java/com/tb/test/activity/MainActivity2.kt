package com.tb.test.activity


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.liaoinstan.springview.widget.SpringView
import com.tb.mvvm_library.tbAdapter.BaseRecyclerAdapter
import com.tb.mvvm_library.tbExtend.*
import com.tb.mvvm_library.tbInterface.WebViewListener
import com.tb.mvvm_library.uiActivity.TbBaseTitleActivity
import com.tb.mvvm_library.util.LogUtils
import com.tb.mvvm_library.util.SpanUtils
import com.tb.mvvm_library.view.FloatingItemDecoration
import com.tb.mvvm_library.view.TbPopupWindow
import com.tb.test.R
import com.tb.test.databinding.ItemBinding
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity2 : TbBaseTitleActivity(), SpringView.OnFreshListener {

    val listData = arrayListOf<String>()
    lateinit var adapter: TestAdapter

    override fun onLoadmore() {

        object : Thread() {
            override fun run() {
                sleep(2000)
                runOnUiThread {
                    springView.onFinishFreshAndLoad()
                    val size = listData.size
                    listData.addAll(listData)
                    adapter.notifyItemRangeInserted(size, listData.size)
                    LogUtils.log("CESHI")
                }
            }
        }.start()

    }

    override fun onRefresh() {

        object : Thread() {
            override fun run() {
                sleep(2000)
                runOnUiThread {
                    springView.onFinishFreshAndLoad()
                    listData.clear()
                    listData.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")
                    listData.add("http://pic.rmb.bdstatic.com/f54083119edfb83c4cfe9ce2eeebc076.jpeg")
                    listData.add("http://img.sccnn.com/bimg/337/23662.jpg")
                    listData.add("http://www.leawo.cn/attachment/201404/16/1433365_1397624557Bz7w.jpg")
                    listData.add("http://pic13.nipic.com/20110425/668573_150157400119_2.jpg")
                    listData.add("http://pic55.nipic.com/file/20141203/12953641_194137094000_2.jpg")
                    listData.add("http://img02.tooopen.com/Downs/images/2010/7/14/sy_20100714115734724071.jpg")
                    listData.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")
                    listData.add("http://pic.rmb.bdstatic.com/f54083119edfb83c4cfe9ce2eeebc076.jpeg")
                    adapter.notifyDataSetChanged()
                }
            }
        }.start()

    }


    var mSearchButton: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        tbStatusBarInit(statusColorId = R.color.transparent, isFitWindowStatusBar = true)
        rootLayoutId = R.layout.activity_main2
        super.onCreate(savedInstanceState)

        val images = arrayListOf(
            "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565247465&di=6e39ae6182849bf4fc1dc5d4ef2867ab&src=http://pic.rmb.bdstatic.com/cd2476300bbad8dfcfff1d277b79401a.jpeg"
        )
//        mLineGrid.setUrls(images)
//        mLineGrid.onItemClick = { position, info ->
//            tbShowToast("$position")
//        }
        mMenu.itemClick = {
            listData.removeAt(0)
            adapter.notifyItemRemoved(0)
        }

        mMenu.setIconLeft(R.drawable.icon_print_computer).setLeftTx("小米").setRightTx("手机类型")

        mBanner.initBanner(images, itemClick = { position ->
            tbShowToast("$position")
        }).stopTurning()
        initToolBar(bgColorArgb = Color.argb(0, 255, 0, 0), paddingTop = tbStatusBarHeight()[0])
        mSearchView?.init(onSearchClick = {
            tbShowToast("点击")
        }, getViews = { mSearchButton, mCloseButton, mCollapsedButton, mSearchAutoComplete ->
            this.mSearchButton = mCollapsedButton
        }, onQueryChange = {
            LogUtils.log("onQueryChange---->$it")
        }, onQuerySubmit = {
            LogUtils.log("onQuerySubmit---->$it")
        })

        (mSearchView?.background!! as GradientDrawable).setTint(Color.argb((0).toInt(), 255, 255, 255))
//        setTitleLeft("测试")
//        setTitleCenter("蓝调")
        initMenu(arrayListOf("搜索"), menuClick = { position, view ->
            val pop = TbPopupWindow(this, R.layout.pop_test, isOutsideTouchable = true)
            pop.showAtLocationBottom(view, 0)
        })

        springView.init(this)

        listData.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")
        listData.add("http://pic.rmb.bdstatic.com/f54083119edfb83c4cfe9ce2eeebc076.jpeg")
        listData.add("http://img.sccnn.com/bimg/337/23662.jpg")
        listData.add("http://www.leawo.cn/attachment/201404/16/1433365_1397624557Bz7w.jpg")
        listData.add("http://pic13.nipic.com/20110425/668573_150157400119_2.jpg")
        listData.add("http://pic55.nipic.com/file/20141203/12953641_194137094000_2.jpg")
        listData.add("http://img02.tooopen.com/Downs/images/2010/7/14/sy_20100714115734724071.jpg")
        listData.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")
        listData.add("http://pic.rmb.bdstatic.com/f54083119edfb83c4cfe9ce2eeebc076.jpeg")
        listData.add("http://img.sccnn.com/bimg/337/23662.jpg")
        listData.add("http://www.leawo.cn/attachment/201404/16/1433365_1397624557Bz7w.jpg")
        listData.add("http://pic13.nipic.com/20110425/668573_150157400119_2.jpg")
        listData.add("http://pic55.nipic.com/file/20141203/12953641_194137094000_2.jpg")
        listData.add("http://img02.tooopen.com/Downs/images/2010/7/14/sy_20100714115734724071.jpg")
        listData.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")

        val keys = mutableMapOf<Int, String>()
        keys.put(0, "欧美大片")
        keys.put(2, "国产剧透")
        keys.put(4, "印度神剧")
        val itemd = FloatingItemDecoration(
            this,
            R.color.line_background,
            tbGetDimensValue(R.dimen.x1),
            tbGetDimensValue(R.dimen.x1)
        )
        itemd.setKeys(keys)
            .setTitleHeight(tbGetDimensValue(R.dimen.x80)).init(this, R.color.colorAccent)


        adapter = TestAdapter(listData, R.layout.item)

        mRecyclerView.init(
            listData,
            adapter,
            mLayoutManager = StaggeredGridLayoutManager::class.java,
            dividerColor = R.color.white,
            dividerSize = tbGetDimensValue(R.dimen.x10),
            itemClick = { position ->
                tbShowToast("$position")
            },
            floatTitleDecoration = itemd,
            scrollYListener = { scrollY, isTopDirection ->
                LogUtils.log("tb----->$scrollY")
            }, headerViews = arrayListOf(R.layout.item)
        ).forEach {
            if (it is ItemBinding) {
                it.root.background = ContextCompat.getDrawable(this, R.color.tb_green)
            }
        }

        val ll: RelativeLayout.LayoutParams = mSearchView?.layoutParams as RelativeLayout.LayoutParams

        image.post {
            mAppBarLayout.scrollScale(image.measuredHeight * 1f - mToolbar.measuredHeight) { scaleValue, scrollY ->
                mToolbar.setBackgroundColor(Color.argb((scaleValue * 255).toInt(), 255, 0, 0))
                mSearchButton?.setColorFilter(Color.argb((scaleValue * 255).toInt(), 0, 0, 255))
                mSearchView?.background?.setTint(Color.argb((scaleValue * 255).toInt(), 255, 255, 255))

                mSearchView?.translationY = -scaleValue * 300
            }
        }

        mWebView.init("https://www.baidu.com", loadListener = object : WebViewListener {
            override fun loadStart(view: WebView?, url: String?, favicon: Bitmap?) {
                LogUtils.log("loadStart------")


            }

            override fun loading(view: WebView?, newProgress: Int) {
                LogUtils.log("loading------$newProgress")
            }

            override fun loadComplete(view: WebView?, url: String?) {
                LogUtils.log("loadComplete------")
            }
        })
    }

}

class TestAdapter(
    listData: ArrayList<*>, @LayoutRes layoutId: Int
) : BaseRecyclerAdapter(listData, layoutId) {
    override fun onBind(holder: MyHolder, position: Int) {
        when (holder.itemBinding) {
            is ItemBinding -> {
                val itemBind: ItemBinding = holder.itemBinding as ItemBinding

                itemBind.str = listData[position] as String

                itemBind.tex.showImage(itemBind.str.toString())

                itemBind.tex.tbImageLongPress(itemBind.root.context as AppCompatActivity)

                itemBind.root.setOnClickListener {
                    tbItemClick?.invoke(position)
                }
            }
        }

    }

}


