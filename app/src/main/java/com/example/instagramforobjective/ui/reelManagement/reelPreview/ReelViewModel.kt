package com.example.instagramforobjective.ui.reelManagement.reelPreview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.getCurrentUserId
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelViewModel : ViewModel() {
    private val _userReelLiveData = MutableLiveData<List<Reel>>()
    val reelLiveData: LiveData<List<Reel>> = _userReelLiveData

    fun getUserReels(){
        val currentUserUid = getCurrentUserId()
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.REEL)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<Reel>()
                    for (document in postSnapshot.documents) {
                        val post = document.toObject<Reel>()
                        if (post != null) {
                            tempList.add(post)
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

}