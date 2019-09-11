package com.tb.mvvm_library.tbAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tb.design.library.tbUtil.FontUtil
import com.tb.mvvm_library.base.TbConfigure
import com.tb.mvvm_library.tbExtend.TbItemClick
import com.tb.mvvm_library.tbExtend.TbItemClickInfo

/**
 *@作者：tb
 *@时间：2019/8/14
 *@描述：RecyclerView 适配器基类
 */
abstract class BaseRecyclerAdapter(var listData: ArrayList<*>, @LayoutRes var layoutId: Int ) :
    RecyclerView.Adapter<BaseRecyclerAdapter.MyHolder>() {

    var tbItemClick: TbItemClick = null//item点击事件

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemBing: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        if (TbConfigure.getInstance().fontType.isNotEmpty()) {
            FontUtil.replaceFont(itemBing.root, TbConfigure.getInstance().fontType)
        }
        return MyHolder(itemBing)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        onBind(holder, position)
        holder.itemBinding.executePendingBindings()
    }

    abstract fun onBind(holder: MyHolder, position: Int)


    class MyHolder(mItemBinding: ViewDataBinding) : RecyclerView.ViewHolder(mItemBinding.root) {
        var itemBinding: ViewDataBinding = mItemBinding
    }

}