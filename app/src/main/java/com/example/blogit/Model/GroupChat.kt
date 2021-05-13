package com.example.blogit.Model

class GroupChat {

    var sender: String? = null
    var senderName: String? = null
    var groupIDReceiver: String? = null
    var groupNameReceiver: String? = null
    var creationtime: Long = System.currentTimeMillis()
    var message: String? = null
    var timestamp: String? = null

    constructor() {

    }

    constructor(sender: String? , senderName: String?, groupIDReceiver: String?, groupNameReceiver: String?, message: String?, timestamp: String?) {
        this.sender = sender
        this.senderName = senderName
        this.groupIDReceiver = groupIDReceiver
        this.groupNameReceiver = groupNameReceiver
        this.message = message
        this.timestamp = timestamp

    }

}