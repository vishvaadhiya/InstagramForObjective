package com.example.instagramforobjective.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.PostListItemBinding
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.PreferenceHelper
import com.example.instagramforobjective.utility.showToast
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class HomeAdapter(var context: Context, var postList:ArrayList<Post>, private val preferenceHelper: PreferenceHelper) : BaseAdapter() {
    override fun getDataAtPosition(position: Int): Any {
        return postList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.post_list_item
    }

    @SuppressLint("SuspiciousIndentation")
    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding is PostListItemBinding){
            Firebase.firestore.collection(Constants.USER).document(postList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<UserModel>()
                        Glide.with(context)
                            .load(user?.image)
                            .placeholder(R.drawable.user)
                            .into(viewDataBinding.profileImage)

                    viewDataBinding.nameTextView.text = user?.name
                }
            viewDataBinding.homePostCaption.text = postList[position].caption
            val timeAgo = TimeAgo.using(postList[position].time.toLong())
            viewDataBinding.timeTextView.text = timeAgo

            Glide.with(context)
                .load(postList[position].postUrl)
                // .placeholder(R.drawable.ic_loading)
                .into(viewDataBinding.myPostImage)

            //shared button
            viewDataBinding.homeShareImage.setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody = context.getString(R.string.here_is_the_share_content_body_download_our_app_from_google_play_store_https_play_google_com_store_apps)
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
            }

            //like button

            viewDataBinding.homeLikeImage.setOnClickListener {
                postList[position].isLikedImage = !postList[position].isLikedImage
                if (postList[position].isLikedImage) {
                    viewDataBinding.homeLikeImage.setImageResource(R.drawable.fill_heart)
                } else {
                    viewDataBinding.homeLikeImage.setImageResource(R.drawable.heart)
                }
            }

            //save button
            viewDataBinding.homeSaveImage.setOnClickListener {
                val post = postList[position]

                if (!post.isSavedImage) {
                    post.isSavedImage = true
                    viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                    val firestore = FirebaseFirestore.getInstance()
                    val collectionRef = firestore.collection(Constants.SAVED_POST)
                    collectionRef.whereEqualTo("postUrl", post.postUrl)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val newDocumentRef = collectionRef.document()

                                val data = hashMapOf(
                                    "postUrl" to post.postUrl,
                                    "caption" to post.caption,
                                    "uid" to post.uid
                                )

                                newDocumentRef.set(data)
                                    .addOnSuccessListener {
                                        context.showToast(context.getString(R.string.successfully_saved))
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("TAG", "Exception: $e")
                                    }
                            } else {
                                viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("TAG", "Exception: $e")
                        }
                } else {
                    viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                }
            }

            if (!postList.get(position).isSavedImage) {
                viewDataBinding.homeSaveImage.setImageResource(R.drawable.ic_save)
            } else {
                viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
            }
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updatePosts(newPosts: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(postList, newPosts))
        postList.clear()
        postList.addAll(newPosts)
        postList.sortBy { it.time }
        diffResult.dispatchUpdatesTo(this)
    }


    private class PostDiffCallback(
        private val oldList: List<Post>,
        private val newList: List<Post>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].postId == newList[newItemPosition].postId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}