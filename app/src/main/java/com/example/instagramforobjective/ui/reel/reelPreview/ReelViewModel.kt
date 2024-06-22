package com.example.instagramforobjective.ui.reel.reelPreview

import androidx.lifecycle.ViewModel

class ReelViewModel(private val repository: ReelRepository) : ViewModel() {
    fun getReel(){
        repository.getReel()
    }

}