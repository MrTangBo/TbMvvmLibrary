package com.tb.mvvm_library.tbExtend

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.text.Layout
import android.text.StaticLayout
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.listener.OnPageChangeListener
import com.flyco.roundview.RoundTextView
import com.flyco.roundview.RoundViewDelegate
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.google.android.material.appbar.AppBarLayout
import com.liaoinstan.springview.container.BaseFooter
import com.liaoinstan.springview.container.BaseHeader
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.container.DefaultHeader
import com.liaoinstan.springview.widget.SpringView
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbApplication
import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.tbAdapter.BaseRecyclerAdapter
import com.tb.mvvm_library.tbInterface.WebViewListener
import com.tb.mvvm_library.util.LogUtils
import com.tb.mvvm_library.util.SpanUtils
import com.tb.mvvm_library.view.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

/**
 *@作者：tb
 *@时间：2019/7/12
 *@描述：
 */

/*时间倒计时*/
fun TextView.tbCountDownTime(
    mHander: Handler,
    unableBg: Int = 0,
    enableBg: Int = 0,
    unableTxColor: Int = R.color.tb_text_dark,
    enableTxColor: Int = R.color.tb_text_black
): TextView {
    val mContext = this.context
    var totalTime = 60
    val view = this
    view.isEnabled = false
    view.background = if (unableBg == 0) null else ContextCompat.getDrawable(mContext, unableBg)
    view.setTextColor(ContextCompat.getColor(mContext, unableTxColor))
    mHander.post(object : Runnable {
        override fun run() {
            view.text = context?.getString(R.string.wait_second, totalTime)
            if (mContext is Activity) {
                if (mContext.isDestroyed) {
                    mHander.removeCallbacksAndMessages(null)
                    return
                }
            }
            if (totalTime == 0) {
                mHander.removeCallbacksAndMessages(null)
                view.isEnabled = true
                view.background = if (enableBg == 0) null else ContextCompat.getDrawable(mContext, enableBg)
                view.setTextColor(ContextCompat.getColor(mContext, enableTxColor))
                view.text = "获取验证码"
                return
            }
            totalTime--
            mHander.postDelayed(this, 1000)
        }
    })
    return this
}

/*SpringView初始化*/
fun SpringView.init(
    listener: SpringView.OnFreshListener,
    header: BaseHeader = DefaultHeader(this.context),
    footer: BaseFooter = DefaultFooter(this.context),
    springType: SpringView.Type = SpringView.Type.OVERLAP,
    springGive: SpringView.Give = SpringView.Give.BOTH
): SpringView {
    this.header = header
    this.footer = footer
    this.setListener(listener)
    this.setGive(springGive)
    this.type = springType
    this.setMoveTime(500)
    return this
}

/*初始化CommonTabLayout*/
fun CommonTabLayout.init(
    titles: ArrayList<String>,
    selectIcons: ArrayList<Int> = arrayListOf(),
    unSelectIcons: ArrayList<Int> = arrayListOf()
): CommonTabLayout {
    val mTabEntities = arrayListOf<CustomTabEntity>()
    titles.forEachIndexed { index, s ->
        val enty = object : CustomTabEntity {
            override fun getTabUnselectedIcon(): Int {
                return if (unSelectIcons.isEmpty())
                    0
                else
                    unSelectIcons[index]
            }

            override fun getTabSelectedIcon(): Int {
                return if (selectIcons.isEmpty())
                    0
                else
                    selectIcons[index]
            }

            override fun getTabTitle(): String {
                return s
            }
        }
        mTabEntities.add(enty)
    }
    setTabData(mTabEntities)
    return this
}

/*初始化图片轮播设置*/
fun ConvenientBanner<*>.initBanner(
    imageUrls: ArrayList<*>,
    autoTime: Long = 5000L,
    isCanLoop: Boolean = true,
    @LayoutRes layoutId: Int = R.layout.tb_item_banner,
    scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
    @DrawableRes indicator: Int = 0,
    @DrawableRes indicatorFocus: Int = 0,
    align: ConvenientBanner.PageIndicatorAlign = ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL,
    itemClick: TbItemClick = null,
    onPageSelected: TbItemClick = null,
    onScrolled: TbOnScrolled = null,
    onScrollStateChanged: TbOnScrollStateChanged = null,
    indicatorMarginRect: Rect? = null,//
    itemMarginRect: Rect = Rect(tbGetDimensValue(R.dimen.x20), 0, tbGetDimensValue(R.dimen.x20), 0),
    circleSizeRect: Rect = Rect(0, 0, 0, 0)
): ConvenientBanner<*> {

    (this as ConvenientBanner<Any>).setPages(object : CBViewHolderCreator {
        override fun createHolder(itemView: View?): TbBannerHolderView<*> {
            return TbBannerHolderView<Any>(itemView, circleSizeRect, itemMarginRect, scaleType)
        }

        override fun getLayoutId(): Int {
            return layoutId
        }
    }, imageUrls)
        //设置两个点图片作为翻页指示器，不设置则没有指示器
        .setPageIndicator(intArrayOf(indicator, indicatorFocus))
        .setPageIndicatorAlign(align)
        .setOnPageChangeListener(object : OnPageChangeListener {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                onScrolled?.invoke(recyclerView, dx, dy)
            }

            override fun onPageSelected(index: Int) {
                onPageSelected?.invoke(index)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                onScrollStateChanged?.invoke(recyclerView, newState)
            }
        })
        .setOnItemClickListener {
            itemClick?.invoke(it)
        }.startTurning(autoTime).isCanLoop = isCanLoop

    /*设置指示器的边距*/
    if (indicatorMarginRect != null) {
        try {
            val field = ConvenientBanner::class.java.getDeclaredField("loPageTurningPoint")
            field.isAccessible = true
            val viewGroup: ViewGroup = field.get(this) as ViewGroup
            val layoutParams = viewGroup.layoutParams as RelativeLayout.LayoutParams
            layoutParams.setMargins(
                indicatorMarginRect.left,
                indicatorMarginRect.top,
                indicatorMarginRect.right,
                indicatorMarginRect.bottom
            )
        } catch (e: Exception) {
        }
    }
    return this
}

/*RecyclerView初始化*/
fun RecyclerView?.init(
    adapter: BaseRecyclerAdapter?,
    mLayoutManager: Class<*> = LinearLayoutManager::class.java,
    mSpanCount: Int = 2,
    itemClick: TbItemClick = null,
    scrollYListener: ((scrollY: Int, isTopDirection: Boolean) -> Unit)? = null,//isTop代表是否上滑
    reverseLayout: Boolean = false,//是否倒叙
    orientation: Int = RecyclerView.VERTICAL,
    dividerOrientation: Int = TbConfigure.HORIZONTAL_LIST,//分割线的样式
    dividerColor: Int = R.color.line_background,//分割线的颜色
    dividerSize: Int = this!!.tbGetDimensValue(R.dimen.x1),//分割线的宽度或者高度
    headerViews: ArrayList<Int> = arrayListOf(),//添加头部
    footerViews: ArrayList<Int> = arrayListOf(),//添加尾部
    floatTitleDecoration: FloatingItemDecoration? = null//是否吸附title
): ArrayList<ViewDataBinding> {
    val b: ArrayList<ViewDataBinding> = arrayListOf()
    if (this == null) {
        return b
    }
    if (adapter == null) {
        tbShowToast("适配器不能为空！")
        return b
    }
    var scrollY = 0
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollY += dy
            scrollYListener?.invoke(scrollY, dy > 0)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val first = IntArray(2)
            if (mLayoutManager.simpleName == "StaggeredGridLayoutManager") {
                val l: StaggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager
                l.findFirstCompletelyVisibleItemPositions(first)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    l.invalidateSpanAssignments()//这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
                }
            }
        }
    })

    when (mLayoutManager.simpleName) {
        /*线性布局 包括垂直和水平*/
        "LinearLayoutManager" -> {
            layoutManager = LinearLayoutManager(this.context, orientation, reverseLayout)
            if (floatTitleDecoration != null) {
                addItemDecoration(floatTitleDecoration)
            } else {
                addItemDecoration(
                    TbDividerItemDecoration(
                        context,
                        dividerOrientation,
                        dividerSize,
                        ContextCompat.getColor(context, dividerColor)
                    )
                )
            }
        }
        "GridLayoutManager" -> {
            layoutManager = GridLayoutManager(this.context, mSpanCount)
            addItemDecoration(
                TbDividerItemDecoration(
                    context,
                    TbConfigure.BOTH_SET,
                    dividerSize,
                    ContextCompat.getColor(context, dividerColor)
                )
            )
        }
        "StaggeredGridLayoutManager" -> {
            layoutManager = StaggeredGridLayoutManager(mSpanCount, orientation)
        }

        "TbFlowLayoutManager" -> {
            layoutManager = TbFlowLayoutManager()
        }
    }
    this.adapter = adapter
    adapter.tbItemClick = itemClick

    if (this is TbHeaderRecyclerView) {
        //添加头部
        if (headerViews.isNotEmpty()) {
            headerViews.forEach {
                val bind: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), it, this, false)
                b.add(bind)
                addHeaderView(bind.root)
            }
        }
        //添加尾部
        if (footerViews.isNotEmpty()) {
            footerViews.forEach {
                val bind: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), it, this, false)
                b.add(bind)
                addFooterView(bind.root)
            }
        }
    }
    return b
}

/*AppBarLayout 根据制定的滑动高度得到比例因子,达到透明度的变化*/
fun AppBarLayout.scrollScale(targetHeight: Float, scaleValue: ((scaleValue: Float, scrollY: Int) -> Unit)) {
    var scale = 0f
    this.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
        scale = Math.abs(p1) / targetHeight
        if (scale > 1) {
            scale = 1f
        } else if (scale < 0) {
            scale = 0f
        }
        scaleValue.invoke(scale, p1)
        LogUtils.log("tb--->$scale")
    })
}

/*初始化 LoadingLayout*/
fun LoadingLayout?.initLoadingLayout(
    errorClick: TbOnClick = null,
    emptyClick: TbOnClick = null,
    emptyImgId: Int = TbConfigure.getInstance().emptyIcon,
    errorImgId: Int = TbConfigure.getInstance().errorIcon,
    emptyDescribe: CharSequence = "暂无数据",
    errorDescribe: CharSequence = "连接出错！",
    refreshEmpty: CharSequence = "",
    refreshError: CharSequence = "",
    delegate: ((text: RoundTextView) -> Unit)? = null,
    @LayoutRes loadingLayId: Int = R.layout.tb_include_loading,
    @LayoutRes emptyLayId: Int = 0,
    @LayoutRes errorLayId: Int = 0
): LoadingLayout {
    if (emptyLayId != 0) {
        this?.setEmptyView(emptyLayId)
    } else {
        this?.setUi(
            emptyImgId,
            errorImgId,
            emptyDescribe,
            errorDescribe,
            refreshEmpty,
            refreshError,
            delegate = { text ->
                delegate?.invoke(text)
            })
        this?.emptyClick = { view ->
            emptyClick?.invoke()
        }
    }
    if (errorLayId != 0) {
        this?.setErrorView(errorLayId)
    } else {
        this?.setUi(emptyImgId, errorImgId, emptyDescribe, errorDescribe)
        this?.errorClick = { view ->
            errorClick?.invoke()
        }
    }
    this?.setLoadingView(loadingLayId)

    return this!!
}

/*ViewPager监听*/
fun ViewPager.tbOnPageLisener(
    onPageSelected: TbItemClick = null,
    onPageScrolled: TbOnPageScrolled = null,
    onPageScrollStateChanged: TbOnPageScrollStateChanged = null
) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged?.invoke(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected?.invoke(position)
        }
    })
}

/*初始化SearchView*/
fun SearchView.init(
    textColor: Int = R.color.tb_text_black,
    textHitStr: CharSequence = "搜索",
    textHitColor: Int = R.color.tb_text_dark,
    getViews: ((mSearchButton: ImageView, mCloseButton: ImageView, mCollapsedButton: ImageView, mSearchAutoComplete: SearchView.SearchAutoComplete) -> Unit)? = null,
    textSize: Int = tbGetDimensValue(R.dimen.tb_text28),
    searchBg: Int = R.drawable.tb_bg_search,
    @DrawableRes mSearchIcon: Int = R.drawable.icon_search_white,
    @DrawableRes mCloseIcon: Int = R.drawable.icon_close,
    @DrawableRes mCollapsedIcon: Int = R.drawable.icon_search_white,
    isClick: Boolean = false,
    isFocus: Boolean = false,//是否自动
    onSearchClick: TbOnClick = null,
    onExpand: TbOnClick = null,
    closeListener: TbOnClick = null,
    onQueryChange: ((str: String) -> Unit)? = null,//内容变化监听
    onQuerySubmit: ((str: String) -> Unit)? = null//提交监听
) {
    visibility = View.VISIBLE
    background = ContextCompat.getDrawable(this.context, searchBg)

    setIconifiedByDefault(false)
    onActionViewExpanded()

    val mSearchButton: ImageView
    val mCloseButton: ImageView
    val mCollapsedButton: ImageView
    val field1 = this.javaClass.getDeclaredField("mSearchButton")
    val field2 = this.javaClass.getDeclaredField("mCloseButton")
    val field3 = this.javaClass.getDeclaredField("mCollapsedIcon")
    field1.isAccessible = true
    field2.isAccessible = true
    field3.isAccessible = true
    mSearchButton = field1.get(this) as ImageView
    mCloseButton = field2.get(this) as ImageView
    mCollapsedButton = field3.get(this) as ImageView

    mSearchButton.setImageResource(mSearchIcon)
    mCollapsedButton.setImageResource(mCollapsedIcon)
    mCloseButton.setImageResource(mCloseIcon)

    val mSearchAutoComplete: SearchView.SearchAutoComplete = this.findViewById(R.id.search_src_text)
    if (!isFocus) {
        mSearchAutoComplete.clearFocus()
        this.clearFocus()
    }
    mSearchAutoComplete.hint = textHitStr
    mSearchAutoComplete.setHintTextColor(ContextCompat.getColor(this.context, textHitColor))
    mSearchAutoComplete.setTextColor(ContextCompat.getColor(this.context, textColor))
    mSearchAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    if (isClick) {
        mSearchAutoComplete.isFocusable = false
        mSearchAutoComplete.isFocusableInTouchMode = false
        mSearchAutoComplete.setOnClickListener {
            onSearchClick?.invoke()
        }
    }

    //搜索图标按钮(打开搜索框的按钮)的点击事件
    setOnSearchClickListener {
        onExpand?.invoke()
    }

    /*关闭的监听*/
    setOnCloseListener {
        closeListener?.invoke()
        return@setOnCloseListener false
    }

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            onQuerySubmit?.invoke(p0!!)
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            onQueryChange?.invoke(p0!!)
            return false
        }
    })
    getViews?.invoke(mSearchButton, mCloseButton, mCollapsedButton, mSearchAutoComplete)
}

/*初始化WebView*/
@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
fun WebView.init(
    url: String,
    js2Android: Any? = null,//js调用android类对象
    js2AndroidNames: ArrayList<String> = arrayListOf(),//js调用android的方法名集合
    android2Js: String = "",//android调用Js传参
    android2JsCallBack: TbOnClickInfo = null,//android调用Js回调
    loadListener: WebViewListener? = null//android调用Js回调
) {
    val settings = settings
    //缩放操作
    settings.setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
    settings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
    settings.displayZoomControls = true //隐藏原生的缩放控件
    //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
    settings.javaScriptEnabled = true
    //设置自适应屏幕，两者合用
    settings.useWideViewPort = true //将图片调整到适合webview的大小
    settings.loadWithOverviewMode = true // 缩放至屏幕的大小
    //其他细节操作
    settings.cacheMode = WebSettings.LOAD_DEFAULT //关闭webview中缓存
    settings.allowFileAccess = true //设置可以访问文件
    settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
    settings.domStorageEnabled = true//开启DOM storage API功能
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    settings.loadsImagesAutomatically = true //支持自动加载图片
    settings.blockNetworkImage = false//解决图片不显示
    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

    /*js调用Android*/
    js2AndroidNames.forEach {
        addJavascriptInterface(js2Android, it)
    }

    /*js调用Android条用js*/
    evaluateJavascript(android2Js) {
        android2JsCallBack?.invoke(it)
    }
    webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loadListener?.loadStart(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            loadListener?.loadComplete(view, url)
            val params = layoutParams
            params.width = context.tbGetScreenSize()[0]
            params.height = context.tbGetScreenSize()[1]
            layoutParams = layoutParams
        }
    }
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            loadListener?.loading(view, newProgress)
        }
    }
    /*禁止长按事件*/
    /*WebView.HitTestResult.UNKNOWN_TYPE //未知类型
      WebView.HitTestResult.PHONE_TYPE //电话类型
      WebView.HitTestResult.EMAIL_TYPE //电子邮件类型
      WebView.HitTestResult.GEO_TYPE //地图类型
      WebView.HitTestResult.SRC_ANCHOR_TYPE //超链接类型
      WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE //带有链接的图片类型
      WebView.HitTestResult.IMAGE_TYPE //单纯的图片类型
      WebView.HitTestResult.EDIT_TEXT_TYPE //选中的文字类型*/
    setOnLongClickListener {
        true
    }
    loadUrl(url)
}

/*发送通知*/
fun NotificationManager.tbNotify(
    title: String,
    content: String,
    @DrawableRes largeIcon: Int,
    @DrawableRes smallIcon: Int,
    tartActivityClass: Class<*>,
    channelId: String = "default",
    channelName: String = "Default Channel",
    notifyId: Int = 1,
    flags: Int = FLAG_AUTO_CANCEL

): Builder {
    val mBuilder: Builder
    val chan: NotificationChannel
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (getNotificationChannel(channelId) == null) {
            chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            chan.setShowBadge(false)//禁止该渠道使用角标
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            chan.enableLights(true)
            // 设置通知出现时的震动（如果 android 设备支持的话）
            chan.enableVibration(false)
            //如上设置使手机：静止1秒，震动2秒，静止1秒，震动3秒
            chan.vibrationPattern = longArrayOf(1000, 400)
            chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC//设置锁屏是否显示通知
            chan.lightColor = Color.BLUE
            chan.setBypassDnd(true)//设置是否可以绕过请勿打扰模式
            createNotificationChannel(chan)
        }
        mBuilder = Builder(TbApplication.mApplicationContext, channelId)
    } else {
        mBuilder = Builder(TbApplication.mApplicationContext)
        mBuilder.priority = PRIORITY_MAX
        mBuilder.setDefaults(DEFAULT_ALL)
    }
    val peddingIntent = PendingIntent.getActivity(
        TbApplication.mApplicationContext,
        0,
        Intent(TbApplication.mApplicationContext, tartActivityClass).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    mBuilder.setContentTitle(title)
        .setContentText(content)
        .setLargeIcon(
            BitmapFactory.decodeResource(
                TbApplication.mApplicationContext.resources,
                largeIcon
            )
        ).setSmallIcon(smallIcon)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(peddingIntent)
        .setProgress(100, 0, false)
    val n = mBuilder.build()
    n.flags = flags
    notify(notifyId, n)
    return mBuilder
}

/*设置数字标记*/
fun View.tbShowBadgeNum(
    num: Int = 0,
    bgColor: Int = R.color.tb_green,
    txColor: Int = R.color.white,
    moveUpListener: ((badge: Badge, targetView: View) -> Unit)? = null,
    padding: Int = tbGetDimensValue(R.dimen.x10),
    txSize: Int = tbGetDimensValue(R.dimen.x22),
    gravity: Int = Gravity.END or Gravity.TOP
): Badge {
    val bb = QBadgeView(context).bindTarget(this)
        .setBadgeGravity(gravity).setBadgeBackgroundColor(ContextCompat.getColor(context, bgColor))
        .setBadgeTextColor(ContextCompat.getColor(context, txColor))
        .setBadgeTextSize(txSize.toFloat(), false)
        .setBadgePadding(padding.toFloat(), false)
    bb.badgeNumber = num
    if (moveUpListener != null) {
        bb.setOnDragStateChangedListener { dragState, badge, targetView ->
            if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                moveUpListener.invoke(badge, targetView)
            }
        }
    }
    return bb
}

/*初始化查看更多文本*/
fun TextView.initLookAll(
    maxLine: Int = 4,
    color: Int = R.color.orange,
    icons: ArrayList<Int> = arrayListOf(
        R.drawable.icon_down_orange,
        R.drawable.icon_up_orange
    )
) {
    var isExpand = false
    //获取内容
    val content = StringBuffer()
    for (i in text.toString().indices) {
        if ((text.toString()[i] in 'a'..'z') || (text.toString()[i] in 'A'..'Z') || Character.isDigit(text.toString()[i])) {
            content.append("${text.toString()[i]} ")
        } else {
            content.append("${text.toString()[i]}")
        }
    }
    //获取TextView的画笔对象
    val paint = paint
    //每行文本的布局宽度
    val width = (parent as ViewGroup).measuredWidth - paddingStart - paddingEnd - marginLeft - marginEnd
    //实例化StaticLayout 传入相应参数
    val staticLayout = StaticLayout(content, paint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0f, false)
    if (staticLayout.lineCount > maxLine) {
        //定义收缩后的文本内容
        val index = staticLayout.getLineStart(maxLine) - 1
        val strclose = content.substring(0, index - 5)
        //定义展开后的文本内容
        val indexEnd = staticLayout.getLineStart(staticLayout.lineCount) - 1
        val strExpand = content.substring(0, indexEnd - 1)
        SpanUtils.with(this).append(strclose)
            .append("...查看全部")
            .setForegroundColor(ContextCompat.getColor(context, color))
            .appendImage(icons[0], SpanUtils.ALIGN_CENTER).create()
        setOnClickListener {
            if (isExpand) {
                isExpand = false
                SpanUtils.with(this).append(strclose)
                    .append("...查看全部")
                    .setForegroundColor(ContextCompat.getColor(context, color))
                    .appendImage(icons[0], SpanUtils.ALIGN_CENTER).create()

            } else {
                isExpand = true
                SpanUtils.with(this).append(strExpand)
                    .append("\t\t收起")
                    .setForegroundColor(ContextCompat.getColor(context, color))
                    .appendImage(icons[1], SpanUtils.ALIGN_CENTER).create()
            }
        }
    } else {
        text = content
    }
}
