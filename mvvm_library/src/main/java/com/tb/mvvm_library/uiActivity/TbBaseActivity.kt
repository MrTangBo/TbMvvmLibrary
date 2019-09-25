package com.tb.mvvm_library.uiActivity

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tb.design.library.tbUtil.FontUtil
import com.tb.mvvm_library.R
import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.base.TbEventBusInfo
import com.tb.mvvm_library.model.TbBaseModel
import com.tb.mvvm_library.tbDialog.TbLoadingDialog
import com.tb.mvvm_library.tbExtend.tbAddActivity
import com.tb.mvvm_library.tbExtend.tbCleanAllActivity
import com.tb.mvvm_library.tbExtend.tbKeyboard
import com.tb.mvvm_library.tbExtend.tbShowToast
import com.tb.mvvm_library.tbInterface.LoadDialogListener
import com.tb.mvvm_library.util.ActivityManagerUtil
import com.tb.mvvm_library.view.LoadingLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess

abstract class TbBaseActivity : AppCompatActivity(), LoadDialogListener {

    open var baseActivityBing: ViewDataBinding? = null
    open var modelList: ArrayList<TbBaseModel> = arrayListOf()
    open lateinit var mContext: Context

    open var rootLayoutId: Int = 0
    var loadingDialog: TbLoadingDialog? = null
    var loadLayout: LoadingLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.background = ContextCompat.getDrawable(this, R.color.tb_white)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        baseActivityBing = DataBindingUtil.setContentView(this, rootLayoutId)
        init()
        initModel()
        initView()
    }


    open fun init() {
        mContext = this
        initLoadingDialog()
        tbAddActivity()
        EventBus.getDefault().register(this)
    }

    /*初始化网络加载进度条*/
    open fun initLoadingDialog() {
        loadingDialog = TbLoadingDialog(this)
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
            it.mActivity = this
            it.lodDialogListener = this
        }
    }

    open fun addModel() {

    }

    open fun initView() {


    }

    /*显示加载框*/
    override fun showLoadDialog() {

    }

    /*取消加载进度*/
    override fun dismissLoadDialog() {
    }

    open fun onClick(view: View?) {

    }

    override fun onResume() {
        super.onResume()
        if (TbConfigure.getInstance().fontType.isNotEmpty()) {
            FontUtil.replaceFont(this, TbConfigure.getInstance().fontType)
        }
        //设置为竖屏
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        tbKeyboard(false)
        ActivityManagerUtil.getInstance().removeActivity(this)
        EventBus.getDefault().unregister(this)
    }


    /**
     * 退出app处理
     */
    private var exitTime: Long = 0

    open fun exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
        {
            tbShowToast("再点一次退出应用")
            exitTime = System.currentTimeMillis()
        } else {
            tbCleanAllActivity()
            exitProcess(0)
        }
    }

    open lateinit var eventBundle: Bundle
    /*eventBus回调*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onUserEvent(event: TbEventBusInfo) {
        eventBundle = event.bundle
    }


}