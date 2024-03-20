package com.example.instagramforobjective.ui.model

class Story {
    var storyUrl: String = ""
    var storyId: String = ""
    var uid: String = ""
    var time: String = ""
    var isLiked: Boolean = false

    constructor()
    constructor(storyUrl: String, caption: String) {
        this.storyUrl = storyUrl
    }

    constructor(storyUrl: String, uid: String, time: String)
    {
        this.storyUrl = storyUrl
        this.uid = uid.toString()
        this.time = time
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Story
        return storyId == other.storyId
    }

    override fun hashCode(): Int {
        return storyId.hashCode()
    }


}