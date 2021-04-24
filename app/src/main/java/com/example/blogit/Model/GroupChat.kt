package com.example.blogit.Model

class GroupChat {

    var sender: String? = null
    var groupIDReceiver: String? = null
    var groupNameReceiver: String? = null
    var creationtime: Long = System.currentTimeMillis()
    var message: String? = null
    var timestamp: String? = null

    constructor() {

    }

    constructor(sender: String?, groupIDReceiver: String?, groupNameReceiver: String?, message: String?, timestamp: String?) {
        this.sender = sender
        this.groupIDReceiver = groupIDReceiver
        this.groupNameReceiver = groupNameReceiver
        this.message = message
        this.timestamp = timestamp

    }

}