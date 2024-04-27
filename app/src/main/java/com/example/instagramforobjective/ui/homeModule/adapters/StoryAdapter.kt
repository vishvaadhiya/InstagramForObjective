package com.example.instagramforobjective.ui.homeModule.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramforobjective.ui.shared.AddPixActivity
import com.example.instagramforobjective.data.models.Story
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.AddStoryBinding
import com.example.instagramforobjective.databinding.StoryListLayoutBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StoryAdapter(
    private var rootView: View,
    private val context: Context,
    private val storyList: ArrayList<Story>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ADD_STORY = 0
        private const val VIEW_TYPE_STORY = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ADD_STORY -> {
                val addStoryBinding =
                    AddStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddStoryViewHolder(addStoryBinding)
            }

            VIEW_TYPE_STORY -> {
                val storyListLayoutBinding = StoryListLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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

    inner class AddStoryViewHolder(private val binding: AddStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.addPicIv.setImageResource(R.drawable.ic_plus_story)
            binding.addPicIv.setOnClickListener {
                val intent = Intent(context, AddPixActivity::class.java)
                intent.putExtra(Constants.SOURCE, Constants.STORY)
                context.startActivity(intent)
            }
        }
    }

    inner class StoryViewHolder(private val binding: StoryListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Firebase.firestore.collection(Constants.USER).document(storyList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<User>()
                    binding.userNameTxt.text = user?.name
                }
            Glide.with(context)
                .load(storyList[position].storyUrl)
                .placeholder(R.drawable.user)
                .into(binding.profilePic)


            binding.profilePic.setOnClickListener {
                val largerImageUrl = storyList[position].storyUrl
                displayLargerImage(
                    largerImageUrl,
                    binding.profilePic,
                    binding.userNameTxt.text.toString(),
                    TimeAgo.using(storyList[position].time.toLong())
                )
            }
        }
    }

    private fun displayLargerImage(
        largerImageUrl: String,
        profilePic: CircleImageView,
        userName: String,
        time: String,
    ) {
        val largerImageView = ImageView(rootView.context).also {
            it.maxHeight = 100
            it.maxWidth = 200
            it.layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(0, 90, 0, 0)
            }
        }
        val seekBar = SeekBar(rootView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            thumb = null

        }
        val userNameView = TextView(rootView.context).apply {
            text = userName
            textSize = 20f
            setTextColor(Color.WHITE)
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(50, 20, 0, 0)
            }
        }

        val timeView = TextView(rootView.context).apply {
            text = time
            textSize = 10f
            setTextColor(Color.WHITE)
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(50, 70, 0, 0)
            }
        }
        Glide.with(rootView.context)
            .load(largerImageUrl)
            .placeholder(R.drawable.user)
            .into(largerImageView)

        applyBlurEffect()
        val parentView = rootView.parent as ViewGroup
        parentView.addView(largerImageView)
        parentView.addView(seekBar)
        parentView.addView(userNameView)
        parentView.addView(timeView)

        val totalTime = 3000
        val interval = 100
        val progressIncrement = 100 / (totalTime / interval)

        var currentProgress = 0

        val updateProgressJob = CoroutineScope(Dispatchers.Main).launch {
            while (currentProgress < 100) {
                currentProgress += progressIncrement
                seekBar.progress = currentProgress
                delay(interval.toLong())
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(totalTime.toLong())
            parentView.removeView(largerImageView)
            parentView.removeView(seekBar)
            parentView.removeView(userNameView)
            parentView.removeView(timeView)
            rootView.setBackgroundColor(Color.WHITE)
            Blurry.delete(rootView as ViewGroup)
            profilePic.borderColor = Color.BLACK
            updateProgressJob.cancel()
        }

        largerImageView.setOnClickListener {
            parentView.removeView(largerImageView)
            parentView.removeView(seekBar)
            parentView.removeView(userNameView)
            parentView.removeView(timeView)
            rootView.setBackgroundColor(Color.WHITE)
            Blurry.delete(rootView as ViewGroup)
            profilePic.borderColor = Color.BLACK
            updateProgressJob.cancel()
        }

    }

    private fun applyBlurEffect() {
        createBitmapFromView(rootView)

        Blurry.with(rootView.context)
            .radius(10)
            .sampling(8)
            .async()
            .color(Color.BLACK)
            .onto(rootView as ViewGroup)
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.setBackgroundColor(Color.BLACK)
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
