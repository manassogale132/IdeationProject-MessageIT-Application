package com.example.blogit.Model

class Chat {

    var sender : String? = null
    var receiver : String? = null
    var creationtime : Long = System.currentTimeMillis()
    var message : String? = null

    constructor(){

    }

    constructor(sender: String?, receiver: String?, message: String?) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
    }
}