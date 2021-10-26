package de.stenzel.tim.spieleabend.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DominionDeck(
    val cards : List<DominionModel.Expansion.Card>,
    val event : DominionModel.Expansion.Card,
    val monument: DominionModel.Expansion.Card
) : Parcelable
