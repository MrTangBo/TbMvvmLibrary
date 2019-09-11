package com.tb.test.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.flyco.tablayout.CommonTabLayout
import com.tb.mvvm_library.tbDialog.TbLoadingDialog
import com.tb.mvvm_library.tbExtend.*
import com.tb.mvvm_library.tbInterface.LoadDialogListener
import com.tb.mvvm_library.tbZxingUtil.common.Constant.CODED_CONTENT
import com.tb.mvvm_library.uiActivity.TbBaseTitleActivity
import com.tb.test.R
import com.tb.test.TestModle
import com.tb.test.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TbBaseTitleActivity(), LoadDialogListener {

    var model: TestModle? = null

    var bing: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        tbStatusBarInit(statusColorId = R.color.tb_green)
        rootLayoutId = R.layout.activity_main
        super.onCreate(savedInstanceState)
        bottomN.setTitle(
            arrayListOf("首页", "我额", "发布", "你好", "设置"),
            arrayListOf(R.drawable.icon_close, R.drawable.icon_close, R.drawable.icon_close, R.drawable.icon_close, R.drawable.icon_close)
        ).setBadgeNumSingle(0, 30,moveUpListener = {badge, targetView ->  })
//        initToolBar("")
//        initMenu(
//            arrayListOf("打印", R.drawable.tb_back_white)
//        )
//
//        setTitleCenter("asdadsad")
//
//
//        requestPermission(
//            arrayListOf(
//                PermissionItem(Manifest.permission.CAMERA, "相机", R.drawable.permission_ic_camera),
//                PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_camera))
//            ,colorId = R.color.white,styleId = R.style.PermissionDefaultBlueStyle
//        ,permissionFault = {
//
//            },permissionSuccess = {
//
//            }
//        )
//        val listData = arrayListOf<String>()
//        listData.add("ssss")
//        listData.add("ssss")
//        listData.add("ssss")
//        listData.add("ssss")
//        listData.add("ssss")
//        val adapter = TestAdapter(list, listData, R.layout.item)
//
//        adapter.itemClick = {
//            tbShowToast("$it")
//        }
//
//        list.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
//        list.adapter = adapter
//
//        menuClick = {
//            tbShowToast("${it}")
//        }

//        tx.setOnClickListener {
        //            val pop = TbPopupWindow(
//                this,
//                R.layout.pop_test,
//                windowWith = ViewGroup.LayoutParams.MATCH_PARENT
//
//
//            )
//            val bi: PopTestBinding = pop.popBaseBind as PopTestBinding
//            pop.showAtLocationBottom(it,0)

//        }

        val images = arrayListOf<String>()
        images.add("http://photocdn.sohu.com/20130925/Img387224863.jpg")
        images.add("http://pic.rmb.bdstatic.com/f54083119edfb83c4cfe9ce2eeebc076.jpeg")
        images.add("http://img.sccnn.com/bimg/337/23662.jpg")
        images.add("http://www.leawo.cn/attachment/201404/16/1433365_1397624557Bz7w.jpg")
        images.add("http://pic13.nipic.com/20110425/668573_150157400119_2.jpg")
        images.add("http://pic55.nipic.com/file/20141203/12953641_194137094000_2.jpg")
        images.add("http://img02.tooopen.com/Downs/images/2010/7/14/sy_20100714115734724071.jpg")

        mBanner.initBanner(images, itemClick = { position ->
            tbShowToast("$position")
        })

        image.showImage(images[0], scaleType = ImageView.ScaleType.CENTER_INSIDE)
        image.setOnClickListener {
        }
        image.tbImageLongPress(this)

        tx.tbShowBadgeNum(300)

//        tx.setOnClickListener
        tx.setOnClickListener {
            //            val fragmentDialog = TbSelectPictureDialog()
//            fragmentDialog.apply {
//                pictureClick={
//                    tbShowToast("ss")
//                }
//                takePhotoClick={
//                    tbShowToast("333")
//                }
//                show(supportFragmentManager, "myAlert")
//            }


            //                                    EventBus.getDefault().post(TbEventBusInfo(Bundle()))
//            PictureSelector.create(this@MainActivity)
//                .openGallery(PictureMimeType.ofImage())
//                .theme(R.style.tb1)
//                .imageFormat(PictureMimeType.PNG)
//                .circleDimmedLayer(true)
//                .cropWH(1, 1)
//                .enableCrop(true)
//                .forResult(PictureConfig.CHOOSE_REQUEST)
//            images.clear()
//            images.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1565247465&di=6e39ae6182849bf4fc1dc5d4ef2867ab&src=http://pic.rmb.bdstatic.com/cd2476300bbad8dfcfff1d277b79401a.jpeg")
//            mBanner.notifyDataSetChanged()

            tbStartActivity(
//                MainActivity2::class.java,activityOptions = ActivityOptionsCompat.makeScaleUpAnimation(image,image.width/2,image.height/2,0,0).toBundle())
                MainActivity2::class.java
            )

//            tbStartActivity(TbCaptureActivity::class.java, requestCode = 300)

        }

//            listData.add("66666")
//            listData.add("66666")
//            listData.add("66666")
//
//            adapter.notifyDataSetChanged()


//            val maker = TestPrintDataMaker(this, "ss", 300, 255)
//            val executor = PrintExecutor(device, PrinterWriter80mm.TYPE_80)
//            executor.setOnPrintResultListener {
//
//            }
//
//            executor.setDevice(device)
//            executor.doPrinterRequestAsync(maker)

//            val mFOne = TestFragment()
//            val fm = supportFragmentManager
//            val tx = fm.beginTransaction()
//            tx.add(R.id.fragment_content, mFOne, "ONE")
//            tx.commitAllowingStateLoss()

//        }

    }

    override fun addModel() {
        super.addModel()
        model = ViewModelProviders.of(this).get(TestModle::class.java)
        modelList.add(model!!)
    }

    override fun initModel() {
        super.initModel()
        model?.liveData?.observe(this, Observer {
            val list: ArrayList<*> = it as ArrayList<*>
//            bing?.cotent = (list[0] as TestInfo)
//            tbShowToast("${list[1]}")

        })
        loadingDialog?.show()
        model?.getData()
    }

    override fun initView() {
        super.initView()
        bing = baseActivityBing as ActivityMainBinding
        bing?.url = "http://img1.imgtn.bdimg.com/it/u=4026165941,206009312&fm=26&gp=0.jpg"
        bing?.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 扫描二维码/条码回传
        if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val content = data.getStringExtra(CODED_CONTENT)
                tbShowToast(content!!)
            }
        }
    }
}



