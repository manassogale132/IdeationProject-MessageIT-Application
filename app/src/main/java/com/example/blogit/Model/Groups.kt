package com.example.blogit.Model

class Groups {

    var groupID : String? = null
    var groupName : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var addedUserID : String? = null
    var addedUserName : String? = null
    var addedUserTest : String? = null
    constructor(){

    }

    constructor(groupID: String?, groupName: String?) {
        this.groupID = groupID
        this.groupName = groupName
    }

    constructor(addedUserID: String?,addedUserName: String?,addedUserTest: String?) {
        this.addedUserID = addedUserID
        this.addedUserName = addedUserName
        this.addedUserTest = addedUserTest
    }
}