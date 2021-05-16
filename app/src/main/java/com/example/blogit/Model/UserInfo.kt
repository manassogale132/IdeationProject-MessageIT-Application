package com.example.blogit.Model

class UserInfo {

    var userID : String? = null
    var status : String? = null
    var fullName : String? = null
    var age : String? = null
    var emailId : String? = null
    var phoneNumber : String? = null
    var onlineOfflineStatus : String? = null
    var profileimage : String? = null

    constructor(){

    }


    constructor(userID: String?, status: String?, fullName: String?, age: String?, emailId: String?, phoneNumber: String?, onlineOfflineStatus : String?,profileimage : String?) {
        this.userID = userID
        this.status = status
        this.fullName = fullName
        this.age = age
        this.emailId = emailId
        this.phoneNumber = phoneNumber
        this.onlineOfflineStatus = onlineOfflineStatus
        this.profileimage = profileimage
    }

    constructor(
        userID: String?,
        status: String?,
        fullName: String?,
        age: String?,
        emailId: String?,
        phoneNumber: String?
    ) {
        this.userID = userID
        this.status = status
        this.fullName = fullName
        this.age = age
        this.emailId = emailId
        this.phoneNumber = phoneNumber
    }

}