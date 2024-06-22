package com.example.instagramforobjective.ui.searchModule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.SearchItemListBinding

class SearchAdapter(
    private var userList: List<User>,
    private val followButtonClickListener: OnFollowButtonClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: SearchItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.searchNameTv.text = user.name
            Glide.with(binding.userProfileIv.context)
                .load(user.image)
                .placeholder(R.drawable.user)
                .circleCrop()
                .into(binding.userProfileIv)

            binding.followButton.text = if (user.isFollow) {
                binding.root.context.getString(R.string.unfollow)
            } else {
                binding.root.context.getString(R.string.follow)
            }

            binding.followButton.setOnClickListener {
                followButtonClickListener.onFollowButtonClicked(user, !user.isFollow)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newList: List<User>) {
        this.userList = newList
        notifyDataSetChanged()
    }

    interface OnFollowButtonClickListener {
        fun onFollowButtonClicked(user: User, isFollowing: Boolean)
    }
}
