package de.stenzel.tim.spieleabend.models.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DominionDeck(
    val cards : List<DominionCard>,
    val event : DominionEvent,
    val landmark: DominionLandmark
) : Parcelable
