package de.stenzel.tim.spieleabend.models.remote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Designer(
    @PrimaryKey val id: String,
    val num_games: Int,
    val score: Int,
    val game: Game,
    val url: String,
//    val images: Images
)
