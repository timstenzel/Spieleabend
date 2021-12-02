package de.stenzel.tim.spieleabend.models.remote

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val latitude: String? = null,
    val longitude: String? = null
) : Parcelable {
    @IgnoredOnParcel
    var distance = ""
}