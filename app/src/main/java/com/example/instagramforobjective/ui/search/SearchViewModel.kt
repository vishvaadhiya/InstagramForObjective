package com.example.instagramforobjective.ui.search

import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private var repository: SearchRepository) : ViewModel() {

    fun fetchPosts(
        onSuccess: (List<Post>) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.fetchPosts(onSuccess, onError)
    }

    fun fetchUsers(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.fetchUsers(onSuccess, onError)
    }

    fun setRepository(repository: SearchRepository) {
        this.repository = repository
    }
}
