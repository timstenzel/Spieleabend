package de.stenzel.tim.spieleabend.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Publisher(
    @PrimaryKey val id: String,
    val num_games: Int,
    val score: Int,
    val game: Game,
    val url: String,
//    val images: Images
)
