package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.UserPostListItemBinding
import com.example.instagramforobjective.ui.model.Reel

class UserReelRvAdapter(var context: Context, var reelList: ArrayList<Reel>) : BaseAdapter() {

    override fun getDataAtPosition(position: Int): Any {
        return reelList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.user_post_list_item
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        val reel = reelList[position]
        if (viewDataBinding is UserPostListItemBinding) {
            Glide.with(context).load(reel.reelUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(viewDataBinding.myPostImage)
        }

    }

    override fun getItemCount(): Int {
        return reelList.size
    }
}