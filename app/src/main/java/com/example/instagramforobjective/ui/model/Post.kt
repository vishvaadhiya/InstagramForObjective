package com.example.instagramforobjective.ui.model

class Post {
    var postUrl: String = ""
    var postId: String = ""
    var caption: String = ""
    var uid: String = ""
    var time: String = ""
    var isLikedImage: Boolean = false
    var isSavedImage: Boolean = false

    constructor()
    constructor(postUrl: String, caption: String) {
        this.postUrl = postUrl
        this.caption = caption
    }

    constructor(postUrl: String, caption: String, uid: String, time: String,isLikedImage:Boolean)
    {
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid.toString()
        this.time = time
        this.isLikedImage = isLikedImage
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Post
        return postId == other.postId
    }

    override fun hashCode(): Int {
        return postId.hashCode()
    }


}