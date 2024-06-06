package com.example.instagramforobjective.ui.homeModule.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.instagramforobjective.base.BaseAdapter
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.PostListItemBinding
import com.example.instagramforobjective.utils.showToast
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class HomeAdapter(
    var context: Context,
    var postList: ArrayList<Post>,
    private val preferenceHelper: PreferenceHelper,
    var currentID: String?,
) : BaseAdapter() {
    override fun getDataAtPosition(position: Int): Any {
        return postList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.post_list_item
    }

    @SuppressLint("SuspiciousIndentation")
    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding is PostListItemBinding) {
            Firebase.firestore.collection(Constants.USER).document(postList[position].uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<User>()
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
                .into(viewDataBinding.myPostImage)

            //shared button
            viewDataBinding.homeShareImage.setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody =
                    context.getString(R.string.here_is_the_share_content_body_download_our_app_from_google_play_store_https_play_google_com_store_apps)
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
            }

            //like Button
            val post = postList[position]
            viewDataBinding.homeLikeImage.setOnClickListener {
                toggleLikeState(post.postId)
            }
            setLikeIcon(post.postId, viewDataBinding.homeLikeImage)

            //save button
            viewDataBinding.homeSaveImage.setOnClickListener {
                /*val post = postList[position]

                if (!post.isSavedImage) {
                    viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                    val firestore = FirebaseFirestore.getInstance()
                    val collectionRef = firestore.collection(Constants.SAVED_POST)
                    collectionRef.whereEqualTo("postUrl", post.postUrl)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val newDocumentRef = collectionRef.document()

                                val data = hashMapOf(
                                    "postId" to post.postId,
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
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("TAG", "Exception: $e")
                        }
                } else {
                   viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                   context.showToast(context.getString(R.string.already_saved_this_post))
                }*/
                val post = postList[position]

                val firestore = FirebaseFirestore.getInstance()
                val collectionRef = firestore.collection(Constants.SAVED_POST)

                if (!post.isSavedImage) {
                    collectionRef.whereEqualTo("postUrl", post.postUrl)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val newDocumentRef = collectionRef.document()

                                val data = hashMapOf(
                                    "postId" to post.postId,
                                    "postUrl" to post.postUrl,
                                    "caption" to post.caption,
                                    "uid" to post.uid
                                )

                                newDocumentRef.set(data)
                                    .addOnSuccessListener {
                                        toggleSavedState(post.postId)
                                        post.isSavedImage = true
                                        viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                                        context.showToast(context.getString(R.string.successfully_saved))
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("TAG", "Exception: $e")
                                    }
                            } else {
                                context.showToast(context.getString(R.string.already_saved_this_post))
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("TAG", "Exception: $e")
                        }
                } else {
                    viewDataBinding.homeSaveImage.setImageResource(R.drawable.save)
                    context.showToast(context.getString(R.string.already_saved_this_post))
                }
            }
            setSavedIcon(post.postId,viewDataBinding.homeSaveImage)
        }
    }

    private fun toggleLikeState(postId: String) {
        val isLiked = preferenceHelper.loadLikeState(postId, FirebaseAuth.getInstance().currentUser!!.uid)

        val updatedLikeState = !isLiked
        preferenceHelper.saveLikeState(
            postId,
            FirebaseAuth.getInstance().currentUser!!.uid,
            updatedLikeState
        )
    }

    private fun setLikeIcon(postId: String, imageView: ImageView) {
        val isLiked = preferenceHelper.loadLikeState(postId, FirebaseAuth.getInstance().currentUser!!.uid)

        if (isLiked) {
            imageView.setImageResource(R.drawable.fill_heart)
        } else {
            imageView.setImageResource(R.drawable.heart)
        }
    }

    private fun toggleSavedState(postId: String) {
        val isLiked = preferenceHelper.loadLikeState(postId, FirebaseAuth.getInstance().currentUser!!.uid)

        val updatedLikeState = !isLiked
        preferenceHelper.savedState(
            postId,
            FirebaseAuth.getInstance().currentUser!!.uid,
            updatedLikeState
        )
    }

    private fun setSavedIcon(postId: String, imageView: ImageView) {
        val isLiked = preferenceHelper.loadSavedState(postId, FirebaseAuth.getInstance().currentUser!!.uid)

        if (isLiked) {
            imageView.setImageResource(R.drawable.save)
        } else {
            imageView.setImageResource(R.drawable.ic_save)
        }
    }


    override fun getItemCount(): Int {
        return postList.size
    }

    fun updatePosts(newPosts: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(postList, newPosts))
        val temp = newPosts.sortedByDescending { it.time.toLong() }
        postList.clear()
        postList.addAll(temp)
        diffResult.dispatchUpdatesTo(this)
    }


    private class PostDiffCallback(
        private val oldList: List<Post>,
        private val newList: List<Post>,
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