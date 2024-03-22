package com.example.instagramforobjective.ui.dashboard

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentHomeBinding
import com.example.instagramforobjective.ui.dashboard.adapter.HomeAdapter
import com.example.instagramforobjective.ui.dashboard.adapter.StoryAdapter
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.ui.model.Story
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.PreferenceHelper
import com.example.instagramforobjective.utility.ProgressDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private var postList = ArrayList<Post>()
    private var storyList = ArrayList<Story>()
    val pHelpers by lazy {

        PreferenceHelper(requireContext())
    }


    override fun defineLayout(): Int {
        return R.layout.fragment_home
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentHomeBinding
        return this.binding
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initComponent() {
        val preferenceHelper = PreferenceHelper(requireContext())
        val storyAdapter = view?.let { StoryAdapter(it,requireContext(),storyList) }
        binding.storyRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.storyRecyclerView.adapter = storyAdapter



        adapter = HomeAdapter(requireContext(), postList, preferenceHelper,FirebaseAuth.getInstance().currentUser?.uid)
        binding.homeRv.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRv.adapter = adapter

        loadLikeStates()
        val newPostList: List<Post> = arrayListOf()
        adapter.updatePosts(newPostList)

        val storyPostList: List<Story> = arrayListOf()
        storyAdapter?.updatePosts(storyPostList)

        ProgressDialog.showDialog(activity as AppCompatActivity)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
                .get()
                .addOnSuccessListener { followSnapshot ->
                    val followedUsers = followSnapshot.toObjects<UserModel>()

                    lifecycleScope.launch {
                        val userUIDs =
                            followedUsers.map { it.uid } + FirebaseAuth.getInstance().currentUser!!.uid
                        userUIDs.forEach { userUID ->
                            val posts = getPostsFromUser(userUID)
                            postList.addAll(posts)
                            postList.reverse()
                            adapter.notifyDataSetChanged()

                            val story = getStoryFromUser(userUID)
                            storyList.addAll(story)
                            storyList.reverse()
                            storyAdapter?.notifyDataSetChanged()
                            ProgressDialog.hideDialog()
                        }
                    }
                }
        }else{
            Log.d("TAg","demo..")
        }

    }


    private fun loadLikeStates() {
        postList.forEach { post ->
            val isLiked = pHelpers.loadLikeState(post.postId, FirebaseAuth.getInstance().currentUser!!.uid)
            post.isLikedImage = isLiked
        }

        adapter.notifyDataSetChanged()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getPostsFromUser(uid: String): List<Post> {
        return suspendCancellableCoroutine { continuation ->
            Firebase.firestore.collection(Constants.POSTS).whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = ArrayList<Post>()
                    for (postDocument in postSnapshot.documents) {
                        val post: Post = postDocument.toObject<Post>()!!
                        tempList.add(post)
                    }
                    continuation.resume(tempList) {
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Error getting posts: $exception")
                    continuation.resumeWithException(exception)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getStoryFromUser(uid: String): List<Story> {
        return suspendCancellableCoroutine { continuation ->
            Firebase.firestore.collection(Constants.STORY).whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = ArrayList<Story>()
                    for (postDocument in postSnapshot.documents) {
                        val story: Story = postDocument.toObject<Story>()!!
                        tempList.add(story)
                    }
                    continuation.resume(tempList) {
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Error getting posts: $exception")
                    continuation.resumeWithException(exception)
                }
        }
    }

}