package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.AddStoryBinding
import com.example.instagramforobjective.databinding.StoryListLayoutBinding
import com.example.instagramforobjective.ui.dashboard.AddPixActivity
import com.example.instagramforobjective.ui.model.Story
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.blurry.Blurry

class StoryAdapter(
    private var rootView: View,
    private val context: Context,
    private val storyList: ArrayList<Story>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ADD_STORY = 0
        private const val VIEW_TYPE_STORY = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ADD_STORY -> {
                val addStoryBinding = AddStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddStoryViewHolder(addStoryBinding)
            }
            VIEW_TYPE_STORY -> {
                val storyListLayoutBinding = StoryListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StoryViewHolder(storyListLayoutBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ADD_STORY -> {
                val addStoryViewHolder = holder as AddStoryViewHolder
                addStoryViewHolder.bind()
            }
            VIEW_TYPE_STORY -> {
                val storyViewHolder = holder as StoryViewHolder
                storyViewHolder.bind(position - 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return storyList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_ADD_STORY
        } else {
            VIEW_TYPE_STORY
        }
    }

    inner class AddStoryViewHolder(private val binding: AddStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.addPicIv.setImageResource(R.drawable.ic_plus_story)
            binding.addPicIv.setOnClickListener {
                val intent = Intent(context, AddPixActivity::class.java)
                intent.putExtra("source", "story")
                context.startActivity(intent)
            }
        }
    }

    inner class StoryViewHolder(private val binding: StoryListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Firebase.firestore.collection(Constants.USER).document(storyList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<UserModel>()
                    binding.userNameTxt.text = user?.name
                }
            Glide.with(context)
                .load(storyList[position].storyUrl)
                .placeholder(R.drawable.user)
                .into(binding.profilePic)

            binding.profilePic.setOnClickListener {
                val largerImageUrl = storyList[position].storyUrl
                displayLargerImage(largerImageUrl, binding.profilePic)
            }
        }
    }

    private fun displayLargerImage(largerImageUrl: String, profilePic: CircleImageView) {
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

    fun updatePosts(newPosts: List<Story>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(storyList, newPosts))
        val temp = newPosts.sortedByDescending { it.time.toLong() }
        storyList.clear()
        storyList.addAll(temp)
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
