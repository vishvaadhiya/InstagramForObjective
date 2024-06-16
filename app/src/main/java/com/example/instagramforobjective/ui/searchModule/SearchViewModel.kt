package com.example.instagramforobjective.ui.searchModule

import androidx.lifecycle.ViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

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
}
