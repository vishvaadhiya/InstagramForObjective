package com.example.instagramforobjective.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(
    binding.root
) {
    fun bind(obj: Any?) {
        binding.executePendingBindings()
    }

    fun getBinding() : ViewDataBinding {
        return binding
    }
}