package de.stenzel.tim.spieleabend.models.remote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Images(
    val thumb: String,
    val small: String,
    val medium: String,
    val large: String,
    @PrimaryKey val original: String
)
