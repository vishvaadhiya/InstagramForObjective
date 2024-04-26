package com.example.instagram.ui.searchModule

import androidx.lifecycle.ViewModel
import com.example.instagram.data.models.Post
import com.example.instagram.data.models.User
import com.example.instagram.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class SearchViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUserUid = auth.currentUser?.uid


    fun fetchPosts(
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

    fun fetchUser(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ){
        if (currentUserUid != null){
            firestore.collection(Constants.USER)
                .get()
                .addOnSuccessListener{
                    var tempList = ArrayList<User>()
                    for (i in it.documents) {
                        var user: User = i.toObject<User>()!!
                        if (user.email != FirebaseAuth.getInstance().currentUser?.email) {
                            tempList.add(user)
                        }
                    }
                    onSuccess(tempList)
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }
        }else{
            onError("Unknown error")
        }

    }

}