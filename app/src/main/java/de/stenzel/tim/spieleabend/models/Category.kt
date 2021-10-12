package de.stenzel.tim.spieleabend.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey val id: String,
    val url: String
)
