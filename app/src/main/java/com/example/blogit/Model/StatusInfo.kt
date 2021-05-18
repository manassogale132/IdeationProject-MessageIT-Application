package com.example.blogit.Model

class StatusInfo {

    var userID : String? = null
    var statustext : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var timestamp : String? = null
    var pimage : String? = null
    var statusUploadedName : String? = null

    constructor(){

    }

    constructor(userID: String?, statustext: String?, timestamp: String?, pimage: String?,statusUploadedName : String? = null) {
        this.userID = userID
        this.statustext = statustext
        this.timestamp = timestamp
        this.pimage = pimage
        this.statusUploadedName = statusUploadedName
    }


}