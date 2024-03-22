package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.blurry.Blurry

class StoryAdapter(private var rootView:View, var context: Context, var storyList: ArrayList<Story>) : BaseAdapter() {
    override fun getDataAtPosition(position: Int): Any {
        return storyList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.story_list_layout
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding is StoryListLayoutBinding) {
            Firebase.firestore.collection(Constants.USER).document(storyList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<UserModel>()
                    viewDataBinding.userNameTxt.text = user?.name
                }
            Glide.with(context)
                .load(storyList[position].storyUrl)
                .placeholder(R.drawable.user)
                .into(viewDataBinding.profilePic)

            viewDataBinding.profilePic.setOnClickListener {
                val largerImageUrl = storyList[position].storyUrl
                displayLargerImage(largerImageUrl,viewDataBinding.profilePic)

            }
        }
    }
    private fun displayLargerImage(largerImageUrl: String,profilePic:CircleImageView) {
        val largerImageView = ImageView(rootView.context).also {
            it.maxHeight = 100
            it.maxWidth = 200
        }

        Glide.with(rootView.context)
            .load(largerImageUrl)
            .circleCrop()
            .placeholder(R.drawable.user)
            .into(largerImageView)

        applyBlurEffect()
        val parentView = rootView.parent as? ViewGroup
        parentView?.addView(largerImageView)

        largerImageView.setOnClickListener {
            parentView?.removeView(largerImageView)
            Blurry.delete(rootView as ViewGroup)
            profilePic.borderColor = Color.BLACK
        }
    }

    private fun applyBlurEffect() {
        val bitmap = createBitmapFromView(rootView)

        Blurry.with(rootView.context)
            .radius(10)
            .sampling(8)
            .async()
            .color(Color.LTGRAY)
            .animate(500)
            .onto(rootView as ViewGroup)
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
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
        private val newList: List<Story>,
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