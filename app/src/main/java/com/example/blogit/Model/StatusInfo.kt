package com.example.blogit.Model

class StatusInfo {

    var userID : String? = null
    var statustext : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var pimage : String? = null

    constructor(userID: String?, statustext: String?, pimage: String?) {
        this.userID = userID
        this.statustext = statustext
        this.pimage = pimage
    }
}