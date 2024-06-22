package com.example.instagramforobjective.ui.profile.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.data.models.SavedPost
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.getCurrentUserId
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class UserViewModel : ViewModel() {

    private val _userPostsLiveData = MutableLiveData<List<Post>>()
    val postsLiveData: LiveData<List<Post>> = _userPostsLiveData

    private val _userReelLiveData = MutableLiveData<List<Reel>>()
    val reelLiveData: LiveData<List<Reel>> = _userReelLiveData

    private val _userSavedPostLiveData = MutableLiveData<List<SavedPost>>()
    val savedPostLiveData: LiveData<List<SavedPost>> = _userSavedPostLiveData

    private val _userInfoLiveData = MutableLiveData<User?>()
    val userInfoLiveData: LiveData<User?> get() = _userInfoLiveData

    fun fetchUserPost() {
        val currentUserUid = getCurrentUserId()
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.POSTS)
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<Post>()
                    for (document in postSnapshot.documents) {
                        val post = document.toObject<Post>()
                        if (post != null) {
                            tempList.add(post)
                            tempList.sortedByDescending { it.time }
                        }
                    }
                    _userPostsLiveData.value = tempList
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "fetchUserPost: $exception")
                }
        } else {
            Log.d("TAG", "if not find user")
        }
    }

    fun fetchUserReel() {
        val currentUserUid = getCurrentUserId()
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.REEL)
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<Reel>()
                    for (document in postSnapshot.documents) {
                        val reel = document.toObject<Reel>()
                        if (reel != null) {
                            tempList.add(reel)
                        }
                    }
                    _userReelLiveData.value = tempList
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "fetchUserPost: $exception")
                }
        } else {
            Log.d("TAG", "if not find user")
        }
    }

    fun fetchSavedPost() {
        val currentUserUid = getCurrentUserId()
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.SAVED_POST)
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<SavedPost>()
                    for (document in postSnapshot.documents) {
                        val savedPost = document.toObject<SavedPost>()
                        if (savedPost != null) {
                            tempList.add(savedPost)
                        }
                    }
                    _userSavedPostLiveData.value = tempList
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "fetchUserPost: $exception")
                }
        } else {
            Log.d("TAG", "if not find user")
        }
    }

    fun fetchUserProfile() {
        val currentUser = getCurrentUserId() ?: return
        currentUser.let { uid ->
            Firebase.firestore.collection(Constants.USER)
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    _userInfoLiveData.value = user
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "fetchUserProfile: can't find detail")
                }
        }
    }

    fun fetchUserData(onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val userId = getCurrentUserId() ?: return
        FirebaseFirestore.getInstance().collection(Constants.USER)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject<User>()
                if (user != null) {
                    onSuccess.invoke(user)
                } else {
                    onError.invoke("User data not found")
                }
            }
            .addOnFailureListener { e ->
                onError.invoke(e.message ?: "Failed to fetch user data")
            }
    }

    fun updateUser(
        newEmail: String,
        newName: String,
        newPassword: String,
        image : String,
        uid:  String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val user = mutableMapOf<String, Any>()
        user[Constants.EMAIL] = newEmail
        user[Constants.NAME] = newName
        user[Constants.PASSWORD] = newPassword
        user[Constants.Image] = image
        user[Constants.UID] = uid

        val userId = getCurrentUserId() ?: return

        FirebaseFirestore.getInstance().collection(Constants.USER)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener { e ->
                onError.invoke(e.message ?: "An error occurred")
            }
    }
}