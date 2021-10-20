package de.stenzel.tim.spieleabend.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EventModel(
    val img : String? = null,
    val title: String? = null,
    val description: String? = null,
    val public: Boolean? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val publisherID: String? = null,
    val publisher: String? = null,
    val location: String? = null,
)