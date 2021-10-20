package de.stenzel.tim.spieleabend.models

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import de.stenzel.tim.spieleabend.R

@IgnoreExtraProperties
data class NewsModel(
    val img : String? = null,
    val title : String? = null,
    val content : String? = null,
    val publisher : String? = null,
    val publishDate : Long? = null
)