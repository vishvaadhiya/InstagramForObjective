package com.example.instagramforobjective.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType, parent, false
        )
        val appViewHolder = BaseViewHolder(binding)

        appViewHolder.itemView.setOnClickListener {
            onItemClickListener?.invoke(getDataAtPosition(appViewHolder.adapterPosition))
        }
        return appViewHolder
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        val obj = getDataAtPosition(position)
        itemViewDataBinding(holder.getBinding(), position)
        holder.bind(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdAtPosition(position)
    }

    protected abstract fun getDataAtPosition(position: Int): Any
    protected abstract fun getLayoutIdAtPosition(position: Int): Int
    protected abstract fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int)

    var onItemClickListener:((Any?) -> Unit)? = null

}