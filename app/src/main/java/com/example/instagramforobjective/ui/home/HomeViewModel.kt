package com.example.instagramforobjective.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.Story
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : ViewModel() {

    private val _postsLiveData = MutableLiveData<List<Post>>()
    val postsLiveData: LiveData<List<Post>> get() = _postsLiveData

    private val _storiesLiveData = MutableLiveData<List<Story>>()
    val storiesLiveData: LiveData<List<Story>> get() = _storiesLiveData

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchPostsAndStories() {
        Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
            .addSnapshotListener { followSnapshot, _ ->
                val followedUsers = followSnapshot?.toObjects<User>() ?: emptyList()
                followSnapshot?.metadata?.hasPendingWrites()
                GlobalScope.launch(Dispatchers.Main) {
                    val userUIDs =
                        followedUsers.map { it.uid } + FirebaseAuth.getInstance().currentUser!!.uid

                    val allPosts = mutableListOf<Post>()
                    val allStory = mutableListOf<Story>()

                    for (userUID in userUIDs) {
                        val posts = getPostsFromUser(userUID)
                        allPosts.addAll(posts)

                        val stories = getStoryFromUser(userUID)
                        allStory.addAll(stories)
                    }
                    _storiesLiveData.value = allStory.sortedByDescending { it.time }
                    _postsLiveData.value = allPosts.sortedByDescending { it.time }
                }
            }
    }

    private suspend fun getPostsFromUser(uid: String): List<Post> {
        return suspendCoroutine { continuation ->
            Firebase.firestore.collection(Constants.POSTS)
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    postSnapshot?.metadata?.hasPendingWrites()
                    val tempList = ArrayList<Post>()
                    for (postDocument in postSnapshot.documents) {
                        val post: Post = postDocument.toObject<Post>()!!
                        tempList.add(post)
                    }
                    continuation.resume(tempList)
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Error getting posts: $exception")
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun getStoryFromUser(uid: String): List<Story> {
        return suspendCoroutine { continuation ->
            val currentTime: Long = System.currentTimeMillis()

            Firebase.firestore.collection(Constants.STORY)
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { storySnapshot ->
                    storySnapshot?.metadata?.hasPendingWrites()
                    val tempList = ArrayList<Story>()
                    for (storyDocument in storySnapshot.documents) {
                        val storyData = storyDocument.data
                        val timeString = storyData?.get("time") as? String

                        if (!timeString.isNullOrEmpty()) {
                            val storyTime = timeString.toLongOrNull()

                            if (storyTime != null) {
                                val timeDifference = currentTime - storyTime
                                if (timeDifference <= (24 * 60 * 60 * 1000)) {
                                    val story: Story = storyDocument.toObject<Story>()!!
                                    tempList.add(story)
                                }
                            }
                        }
                    }
                    continuation.resume(tempList)
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Error getting stories: $exception")
                    continuation.resumeWithException(exception)
                }
        }
    }
}
