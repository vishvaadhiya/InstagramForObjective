package com.example.instagram.ui.profileManagement.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagram.base.BaseAdapter
import com.example.instagram.data.models.SavedPost
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.UserPostListItemBinding

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateSavedPost(savedPostList: List<SavedPost>){
        this.savedPostList = savedPostList as ArrayList<SavedPost>
        notifyDataSetChanged()
    }
}