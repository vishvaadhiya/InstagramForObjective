package com.example.instagramforobjective.ui.reel.reelPreview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReelViewModel @Inject constructor(private var repository: ReelRepository) : ViewModel() {
    fun getReel(){
        repository.getReel()
    }

    fun setRepository(repository: ReelRepository) {
        this.repository = repository
    }

}