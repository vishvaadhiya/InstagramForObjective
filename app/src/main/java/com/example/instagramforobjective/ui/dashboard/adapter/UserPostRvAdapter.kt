package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.UserPostListItemBinding
import com.example.instagramforobjective.ui.dashboard.PostImageActivity
import com.example.instagramforobjective.ui.model.Post

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
                val intent = Intent(context, PostImageActivity::class.java).apply {
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
}