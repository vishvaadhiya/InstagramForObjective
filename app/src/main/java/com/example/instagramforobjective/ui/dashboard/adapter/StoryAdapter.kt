package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.StoryListLayoutBinding
import com.example.instagramforobjective.ui.model.Story
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class StoryAdapter(var context: Context, var storyList:ArrayList<Story>) : BaseAdapter() {
    override fun getDataAtPosition(position: Int): Any {
        return storyList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.story_list_layout
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding is StoryListLayoutBinding) {
            /*Firebase.firestore.collection(Constants.USER).document(storyList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<UserModel>()
                    viewDataBinding.userNameTxt.text = user?.name
                }*/

            Glide.with(context)
                .load(storyList[position].storyUrl)
                 .placeholder(R.drawable.user)
                .into(viewDataBinding.profilePic)
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }
    fun updatePosts(newPosts: List<Story>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(storyList, newPosts))
        storyList.clear()
        storyList.addAll(newPosts)
        storyList.sortBy { it.time }
        diffResult.dispatchUpdatesTo(this)
    }


    private class PostDiffCallback(
        private val oldList: List<Story>,
        private val newList: List<Story>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].storyId == newList[newItemPosition].storyId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}