package de.stenzel.tim.spieleabend.models

import com.google.firebase.database.IgnoreExtraProperties
import de.stenzel.tim.spieleabend.R

@IgnoreExtraProperties
class NewsModel {

    val img : Int = R.drawable.news_default
    val title : String = ""
    val subtitle : String = ""
    val text : String = ""
    val publisher : String = ""
    val publishedAt : String = ""

    constructor() {
        //default constructor
    }
}