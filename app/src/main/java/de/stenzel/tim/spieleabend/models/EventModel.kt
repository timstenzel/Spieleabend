package de.stenzel.tim.spieleabend.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class EventModel {
    val title: String = ""
    val description: String = ""
    val public: Boolean = true
    val date: Long = -1L
    val publisherID: Int = -1
    val publisher: String = ""
    val location: String = ""

    constructor() {
        //default constructor
    }



}