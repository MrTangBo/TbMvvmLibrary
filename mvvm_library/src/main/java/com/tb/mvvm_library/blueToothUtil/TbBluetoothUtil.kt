package com.tb.mvvm_library.blueToothUtil

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tb.mvvm_library.R
import com.tb.mvvm_library.databinding.DialogBluetoothDeviceBinding
import com.tb.mvvm_library.tbDialog.TbSureDialog
import com.tb.mvvm_library.tbExtend.tbGetDimensValue
import com.tb.mvvm_library.tbExtend.tbShowToast
import com.tb.mvvm_library.view.DividerGridItemDecoration

/**
 *@作者：tb
 *@时间：2019/7/14
 *@描述：蓝牙工具类
 */
class TbBluetoothUtil {

    var bottomDialog: BottomSheetDialog? = null
    var bluetoothAdapter: BluetoothAdapter? = null
    var deviceAdapter: DeviceAdapter? = null
    var deviceList: ArrayList<BluetoothDevice> = arrayListOf()

    private var dlgBluetoothOpen: TbSureDialog? = null

    var itemClick: ((device: BluetoothDevice) -> Unit)? = null

    fun tbBluetoothInit(mContext: Context) {
        if (mContext !is Activity && mContext !is Fragment) return
        val activity: AppCompatActivity = mContext as AppCompatActivity
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            tbShowToast("该设备不支持蓝牙！")
            return
        }
        if (bluetoothAdapter!!.isEnabled) {
            if (bluetoothAdapter?.bondedDevices.isNullOrEmpty()) {
                tbShowToast("未搜索到配对设备,请先配对设备!")
                return
            }
            if (bottomDialog == null) {
                bluetoothAdapter?.bondedDevices?.forEach {
                    deviceList.add(it)
                }
                bottomDialog = BottomSheetDialog(activity)
                val bind: DialogBluetoothDeviceBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(activity),
                        R.layout.dialog_bluetooth_device,
                        null,
                        false
                    )
                bottomDialog?.setContentView(bind.root)
                bind.titleStr = "选择打印设备"
                deviceAdapter = DeviceAdapter(deviceList, R.layout.bluetooth_item)
                bind.deviceRecycler.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                bind.deviceRecycler.addItemDecoration(
                    DividerGridItemDecoration(
                        tbGetDimensValue(R.dimen.x1),
                        0,
                        ContextCompat.getColor(mContext, R.color.line_background),
                        false
                    )
                )
                deviceAdapter?.tbItemClick = {
                    itemClick?.invoke(deviceList[it])
                    bottomDialog?.dismiss()
                }
                bind.deviceRecycler.adapter = deviceAdapter
                bottomDialog?.show()
            } else {
                bottomDialog?.show()
            }
        } else {
            if (dlgBluetoothOpen == null) {
                dlgBluetoothOpen = TbSureDialog(messageTx = "打开蓝牙，以便连接蓝牙打印设备。", sureTx = "允许", cancelTx = "拒绝")
                dlgBluetoothOpen?.apply {
                    val open = bluetoothAdapter!!.isEnabled || bluetoothAdapter!!.enable()
                    this.sureClick = {
                        if (!open) {
                            val requestBluetoothOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            requestBluetoothOn.action = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE
                            requestBluetoothOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                            activity.startActivity(requestBluetoothOn)
                        }
                    }
                    this.show(activity.supportFragmentManager,"Bluetooth")
                }
            } else {
                dlgBluetoothOpen?.show(activity.supportFragmentManager, "")
            }
        }

    }

    /*检测蓝牙是否开启*/
    fun checkBluetooth(): Boolean {
        if (bluetoothAdapter == null) return false
        return bluetoothAdapter!!.isEnabled
    }
}
