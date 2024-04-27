package com.example.instagramforobjective.data.models

class Reel {
    var reelUrl:String=""
    var caption:String = ""
    var uid: String = ""
    var profileLink:String ?= null
    var name:String?=null
    constructor()
    constructor(reelUrl: String, caption: String,uid:String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.uid = uid.toString()

    }


    constructor(reelUrl: String, caption: String, profileLink: String,name:String,uid: String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.name = name
        this.profileLink = profileLink
        this.uid = uid
    }

}