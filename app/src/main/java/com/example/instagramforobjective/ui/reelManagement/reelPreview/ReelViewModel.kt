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

class ReelViewModel(private val repository: ReelRepository) : ViewModel() {
    fun getReel(){
        repository.getReel()
    }

}