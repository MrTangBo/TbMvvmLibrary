package com.tb.mvvm_library.blueToothUtil

import android.bluetooth.BluetoothDevice
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tb.mvvm_library.databinding.BluetoothItemBinding
import com.tb.mvvm_library.tbAdapter.BaseRecyclerAdapter
import com.tb.mvvm_library.tbExtend.tbShowToast

class DeviceAdapter(
    listData: ArrayList<BluetoothDevice>, @LayoutRes layoutId: Int
) : BaseRecyclerAdapter( listData, layoutId) {

    override fun onBind(holder: MyHolder, position: Int) {
        val bind: BluetoothItemBinding = holder.itemBinding as BluetoothItemBinding
        val device: BluetoothDevice = listData[position] as BluetoothDevice
        bind.nameStr = device.name
        bind.typeStr = when (device.bluetoothClass.deviceClass) {
            1664 -> {
                bind.isPrint = true
                "打印设备"
            }
            260 -> {
                bind.isPrint = false
                "电脑设备"
            }
            524 -> {
                bind.isPrint = false
                "智能手机"
            }
            else -> {
                bind.isPrint = false
                "未知设备"
            }
        }

        bind.root.setOnClickListener {
            if (!bind.isPrint!!) {
                tbShowToast("该设备不是打印设备!")
                return@setOnClickListener
            }
            tbItemClick?.invoke(position)
        }
    }

}