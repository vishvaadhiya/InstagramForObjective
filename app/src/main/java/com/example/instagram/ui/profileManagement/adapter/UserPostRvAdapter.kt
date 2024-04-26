package com.example.instagram.ui.profileManagement.adapter

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagram.base.BaseAdapter
import com.example.instagram.ui.profileManagement.shared.ViewPostReelActivity
import com.example.instagram.data.models.Post
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.UserPostListItemBinding

class UserPostRvAdapter(var context: Context, var postList: ArrayList<Post>) : BaseAdapter() {

    override fun getDataAtPosition(position: Int): Any {
        return postList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.user_post_list_item
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        val post = postList[position]
        if (viewDataBinding is UserPostListItemBinding) {
            Glide.with(context).load(post.postUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(viewDataBinding.myPostImage)

            viewDataBinding.myPostImage.setOnClickListener {
                val intent = Intent(context, ViewPostReelActivity::class.java).apply {
                    putExtra("imageUrl", post.postUrl)
                    putExtra("caption", post.caption)
                }
                context.startActivity(intent)
            }
        }

    }


    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newPostList: List<Post>) {
        postList = newPostList as ArrayList<Post>
        notifyDataSetChanged()
    }
}