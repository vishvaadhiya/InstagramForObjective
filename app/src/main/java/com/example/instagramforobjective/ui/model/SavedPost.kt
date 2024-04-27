package com.example.instagramforobjective.ui.model

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