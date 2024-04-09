package com.example.instagramforobjective.ui.model

class Reel {
    var reelUrl:String=""
    var caption:String = ""
    var uid: String = ""
    var name:String=""
    var profileLink:String ?= null
    constructor()
    constructor(reelUrl: String, caption: String) {
        this.reelUrl = reelUrl
        this.caption = caption
    }

    constructor(reelUrl: String, caption: String, profileLink: String,name:String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.name = name
        this.profileLink = profileLink
    }

}