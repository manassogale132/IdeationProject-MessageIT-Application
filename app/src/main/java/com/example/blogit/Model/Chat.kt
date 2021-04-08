package com.example.blogit.Model

class Chat {

    var sender : String? = null
    var receiver : String? = null
    var creationtime : Long = System.currentTimeMillis()
    var message : String? = null
    var timestamp : String? = null
    constructor(){

    }

    constructor(sender: String?, receiver: String?, message: String?, timestamp: String?) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.timestamp = timestamp
    }


}