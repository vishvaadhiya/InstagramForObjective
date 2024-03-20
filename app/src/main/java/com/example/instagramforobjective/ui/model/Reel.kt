package com.example.instagramforobjective.ui.model

class Reel {
    var reelUrl:String=""
    var caption:String = ""
    var uid: String = ""
    var profileLink:String ?= null
    constructor()
    constructor(reelUrl: String, caption: String,uid:String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.uid = uid.toString()

    }

    /*constructor(reelUrl: String, caption: String, profileLink: String) {
        this.reelUrl = reelUrl
        this.caption = caption

        this.profileLink = profileLink
    }*/

}