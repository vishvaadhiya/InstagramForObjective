package com.example.instagramforobjective.ui.search

import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User

interface SearchInterface {
    fun fetchPosts(
        onSuccess: (List<Post>) -> Unit,
        onError: (String) -> Unit
    )

    fun fetchUsers(
        onSuccess: (List<User>) -> Unit,
        onError: (String) -> Unit
    )
}
