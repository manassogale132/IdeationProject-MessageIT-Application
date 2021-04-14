package com.example.blogit.Model

class Groups {

    var groupID : String? = null
    var groupName : String? = null
    var creationTime : Long = System.currentTimeMillis()

    constructor(){

    }

    constructor(groupID: String?, groupName: String?) {
        this.groupID = groupID
        this.groupName = groupName
    }
}