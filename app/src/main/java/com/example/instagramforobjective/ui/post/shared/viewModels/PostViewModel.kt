package com.example.instagramforobjective.ui.post.shared.viewModels

import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.data.models.Story
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import java.util.UUID

class PostViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun postYourImage(
        imageUrl: String?,
        caption: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (imageUrl != null) {
            val postId = UUID.randomUUID().toString()
            val post = Post(
                postId = postId,
                postUrl = imageUrl,
                caption = caption,
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                time = System.currentTimeMillis().toString(),
                false
            )

            Firebase.firestore.collection(Constants.POSTS).document().set(post)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Unknown error")
                }
        } else {
            onError("Please upload image first")
        }
    }

    fun postYourStoryImage(
        imageUrl: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        val story = Story(
            storyUrl = imageUrl!!,
            storyId = UUID.randomUUID().toString(),
            uid = FirebaseAuth.getInstance().currentUser!!.uid,
            time = System.currentTimeMillis().toString()
        )

        com.google.firebase.ktx.Firebase.firestore.collection(Constants.STORY).document().set(story)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Unknown error")
            }
    }



    fun postYourReel(videoUrl: String, caption: String,onSuccess: () -> Unit,
                   onError: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection(Constants.USER)
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    val reel = user?.let {
                        Reel(
                            videoUrl,
                            caption,
                            it.image,
                            it.name,
                            it.uid
                        )
                    }
                    if (reel != null) {
                        firestore.collection(Constants.REEL)
                            .document()
                            .set(reel)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { exception ->
                                onError(exception.message ?: "Unknown error")
                            }
                    } else {
                        onError("Unknown error")
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception.message ?: "Unknown error")
                }
        } else {
            onError("Unknown error")
        }
    }
}