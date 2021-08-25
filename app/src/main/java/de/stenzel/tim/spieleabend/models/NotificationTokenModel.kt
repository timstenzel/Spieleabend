package de.stenzel.tim.spieleabend.models

class NotificationTokenModel {

    var installationId: String = ""
    var token: String = ""
    var timestamp: Long = -1L

    constructor() {
        //empty
    }

    constructor(id: String, token: String, timestamp: Long) {
        this.installationId = id
        this.token = token
        this.timestamp = timestamp
    }

}