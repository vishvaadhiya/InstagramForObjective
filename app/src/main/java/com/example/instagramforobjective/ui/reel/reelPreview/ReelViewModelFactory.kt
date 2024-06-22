package com.example.instagramforobjective.ui.reel.reelPreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReelViewModelFactory(private val repository: ReelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReelViewModel::class.java)) {
            return ReelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}