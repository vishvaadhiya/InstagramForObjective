package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.UserPostListItemBinding
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.ui.model.SavedPost

class UserSavedRvAdapter(var context: Context, var savedPostList: ArrayList<SavedPost>) : BaseAdapter() {

    override fun getDataAtPosition(position: Int): Any {
        return savedPostList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.user_post_list_item
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        val savedPostList = savedPostList[position]
        if (viewDataBinding is UserPostListItemBinding) {
//            viewDataBinding.post=savedPostList
//            viewDataBinding.executePendingBindings()
            Glide.with(context).load(savedPostList.postUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(viewDataBinding.myPostImage)
        }

    }

    override fun getItemCount(): Int {
        return savedPostList.size
    }
}