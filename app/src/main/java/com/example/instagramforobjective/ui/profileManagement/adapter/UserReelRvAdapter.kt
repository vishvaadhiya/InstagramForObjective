package com.example.instagramforobjective.ui.profileManagement.adapter

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramforobjective.base.BaseAdapter
import com.example.instagramforobjective.ui.profileManagement.shared.ViewPostReelActivity
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.UserPostListItemBinding

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

            viewDataBinding.myPostImage.setOnClickListener {
                val intent = Intent(context, ViewPostReelActivity::class.java).apply {
                    putExtra("videoUrl", reelList[position].reelUrl)
                }
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    fun updateReel(newReel:List<Reel>){
        reelList = newReel as ArrayList<Reel>
        notifyDataSetChanged()
    }
}