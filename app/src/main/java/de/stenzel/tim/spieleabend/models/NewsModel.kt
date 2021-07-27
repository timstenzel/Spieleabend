package de.stenzel.tim.spieleabend.models

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import de.stenzel.tim.spieleabend.R

@IgnoreExtraProperties
class NewsModel {

    val img : String = ""
    val title : String = ""
    val subtitle : String = ""
    val text : String = ""
    val publisher : String = ""
    val published : Long = -1L

    constructor() {
        //default constructor
    }
}