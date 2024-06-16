package com.example.instagramforobjective.ui.searchModule

import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class SearchRepository : SearchInterface {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUserUid = auth.currentUser?.uid

    override fun fetchPosts(
        onSuccess: (List<Post>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserUid != null) {
            firestore.collection(Constants.POSTS)
                .get()
                .addOnSuccessListener { it ->
                    val tempList = arrayListOf<Post>()
                    for (document in it.documents) {
                        val post = document.toObject<Post>()
                        post?.let { tempList.add(it) }
                    }
                    onSuccess(tempList)
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }
        } else {
            onError("Unknown error")
        }
    }

    override fun fetchUsers(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserUid != null) {
            firestore.collection(Constants.USER)
                .get()
                .addOnSuccessListener {
                    val tempList = arrayListOf<User>()
                    for (document in it.documents) {
                        val user = document.toObject<User>()
                        if (user != null && user.email != auth.currentUser?.email) {
                            tempList.add(user)
                        }
                    }
                    onSuccess(tempList)
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }
        } else {
            onError("Unknown error")
        }
    }
}
