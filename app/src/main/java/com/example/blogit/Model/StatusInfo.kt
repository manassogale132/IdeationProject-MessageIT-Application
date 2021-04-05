package com.example.blogit.Model

class StatusInfo {

    var userID : String? = null
    var statustext : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var timestamp : String? = null
    var pimage : String? = null

    constructor(){

    }

    constructor(userID: String?, statustext: String?, timestamp: String?, pimage: String?) {
        this.userID = userID
        this.statustext = statustext
        this.timestamp = timestamp
        this.pimage = pimage
    }


}