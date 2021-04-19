package com.example.blogit.Model

class Groups {

    var groupID : String? = null
    var groupName : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var addedUserID : String? = null
    constructor(){

    }



    constructor(groupID: String?, groupName: String?) {
        this.groupID = groupID
        this.groupName = groupName
    }

    constructor(addedUserID: String?) {
        this.addedUserID = addedUserID
    }
}