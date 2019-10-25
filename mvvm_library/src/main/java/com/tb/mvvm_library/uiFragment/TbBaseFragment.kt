package com.tb.mvvm_library.uiFragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tb.design.library.tbUtil.FontUtil
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.base.TbEventBusInfo
import com.tb.mvvm_library.model.TbBaseModel
import com.tb.mvvm_library.tbDialog.TbLoadingDialog
import com.tb.mvvm_library.tbExtend.tbIsMultiClick
import com.tb.mvvm_library.tbExtend.tbKeyboard
import com.tb.mvvm_library.tbInterface.LoadDialogListener
import com.tb.mvvm_library.uiActivity.TbBaseActivity
import com.tb.mvvm_library.view.LoadingLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *@作者：tb
 *@时间：2019/7/11
 *@描述：fragment基类
 */
abstract class TbBaseFragment : Fragment(), LoadDialogListener {

    open var baseFragmentBing: ViewDataBinding? = null
    open var modelList: ArrayList<TbBaseModel> = arrayListOf()
    open lateinit var fActivity: FragmentActivity

    open var isViewCreated = false
    open var isUIVisible = false
    open var isDataLoaded = false
    open var mRootView: View? = null
    open lateinit var inflater: LayoutInflater

    open var rootLayoutId: Int = 0
    var loadingDialog: TbLoadingDialog? = null
    var loadLayout: LoadingLayout? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        if (mRootView == null) {
            baseFragmentBing = DataBindingUtil.inflate(inflater, rootLayoutId, container, false)
            mRootView = baseFragmentBing?.root
            init()
        }
        return mRootView
    }

    open fun init() {
        fActivity = this.activity!!
        initLoadingDialog()
        EventBus.getDefault().register(this)
        initModel()
        Handler().postDelayed({
            initView()
            isViewCreated = true
            lazy()
        }, 20)
    }

    /*初始化网络加载进度条*/
    open fun initLoadingDialog() {
        loadingDialog = TbLoadingDialog(fActivity)
    }

    /*初始化LoadingLayout*/
    open fun initLoadingLayout(
        mLoadLayout: LoadingLayout,
        emptyImgId: Int = R.drawable.icon_empty_data,
        errorImgId: Int = 0,
        emptyDescribe: String = "暂无数据",
        errorDescribe: String = "连接出错！",
        @LayoutRes loadingLayId: Int = R.layout.tb_include_loading
    ) {
        loadLayout = mLoadLayout
        loadLayout?.setLoadingView(loadingLayId)
        loadLayout?.setUi(emptyImgId, errorImgId, emptyDescribe, errorDescribe)
    }

    open fun initModel() {
        addModel()
        modelList.forEach {
            lifecycle.addObserver(it)
            it.mActivity = fActivity as TbBaseActivity
            it.mFragment = this
            it.mBinding = baseFragmentBing
            it.lodDialogListener = this
            it.initModel()
        }
    }

    open fun addModel() {

    }

    open fun initView() {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {//这个方法仅仅工作在FragmentPagerAdapter中，不能被使用在一个普通的activity中。
        super.setUserVisibleHint(isVisibleToUser)
        isUIVisible = isVisibleToUser
        if (isVisibleToUser) {
            lazy()
        }
    }

    open fun lazy() {//这个方法仅仅工作在FragmentPagerAdapter中，不能被使用在一个普通的activity中。
        if (isUIVisible && isViewCreated && !isDataLoaded) {
            loadData()
            isDataLoaded = true
        }
    }

    /*懒加载数据*/
    abstract fun loadData()


    /*显示加载框*/
    override fun showLoadDialog() {

    }

    override fun dismissLoadDialog() {
    }

    open fun onClick(view: View?) {
        if (tbIsMultiClick())return
    }

    override fun onDestroy() {
        super.onDestroy()
        fActivity.tbKeyboard(false)
        EventBus.getDefault().unregister(this)
        modelList.forEach {
            it.onDestroy()
        }
    }

    override fun onResume() {
        super.onResume()
        if (TbConfigure.getInstance().fontType.isNotEmpty()) {
            FontUtil.replaceFont(mRootView, TbConfigure.getInstance().fontType)
        }
    }

    open lateinit var eventBundle: Bundle
    /*eventBus回调*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onUserEvent(event: TbEventBusInfo) {
        eventBundle = event.bundle
    }
}