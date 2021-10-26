package de.stenzel.tim.spieleabend.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class DominionModel(
    val expansions: List<Expansion>
) {
    data class Expansion(
        val id: Int,
        val title: String,
        val cards: List<Card>,
    ) {
        @Parcelize
        data class Card(
            val id: Int,
            val title: String,
            val price: Int,
            val text: String,
        ) : Parcelable
    }
}
