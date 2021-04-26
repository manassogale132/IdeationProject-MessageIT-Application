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


    constructor(addedUserID: String?,addedUserName: String?,groupAdminUid: String?) {
        this.addedUserID = addedUserID
        this.addedUserName = addedUserName
        this.groupAdminUid = groupAdminUid
    }
}