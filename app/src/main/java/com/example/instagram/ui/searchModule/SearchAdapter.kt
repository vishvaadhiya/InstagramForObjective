package com.example.instagram.ui.searchModule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.data.models.User
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.SearchItemListBinding

class SearchAdapter(
    var context: Context,
    private var userList: ArrayList<User>,
    private val followButtonClickListener: OnFollowButtonClickListener,
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.binding.searchNameTv.text = userList[position].name
        Glide.with(context)
            .load(userList[position].image)
            .placeholder(R.drawable.user)
            .circleCrop()
            .into(holder.binding.userProfileIv)


        if (currentUser.isFollow) {
            holder.binding.followButton.text = context.getString(R.string.unfollow)
        } else {
            holder.binding.followButton.text = context.getString(R.string.follow)
        }

        holder.binding.followButton.setOnClickListener {
            followButtonClickListener.onFollowButtonClicked(currentUser, !currentUser.isFollow)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    interface OnFollowButtonClickListener {
        fun onFollowButtonClicked(user: User, isFollowing: Boolean)
    }

}