package com.example.blogit.Model

class Groups {

    var groupID : String? = null
    var groupName : String? = null
    var creationTime : Long = System.currentTimeMillis()
    var addedUserID : String? = null
    var addedUserName : String? = null
    var groupAdminUid : String? = null
    constructor(){

    }

    constructor(groupID: String?, groupName: String?,groupAdminUid: String?) {
        this.groupID = groupID
        this.groupName = groupName
        this.groupAdminUid = groupAdminUid
    }

    constructor(addedUserID: String?,addedUserName: String?) {
        this.addedUserID = addedUserID
        this.addedUserName = addedUserName
    }
}