package com.example.instagramforobjective.data.models

class SavedPost {
    var postUrl: String = ""
    var caption: String = ""
    var uid: String = ""

    constructor()
    constructor(postUrl: String, caption: String, uid: String) {
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid.toString()
    }
}