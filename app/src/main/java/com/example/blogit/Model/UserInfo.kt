package com.example.blogit.Model

class UserInfo {

    var userID : String? = null
    var fullName : String? = null
    var age : String? = null
    var emailId : String? = null
    var passowrd : String? = null
    var phoneNumber : String? = null

    constructor(userID: String?, fullName: String?, age: String?, emailId: String?, passowrd: String?, phoneNumber: String?) {
        this.userID = userID
        this.fullName = fullName
        this.age = age
        this.emailId = emailId
        this.passowrd = passowrd
        this.phoneNumber = phoneNumber
    }
}