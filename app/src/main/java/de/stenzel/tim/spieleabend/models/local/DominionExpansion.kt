package de.stenzel.tim.spieleabend.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dominion_expansions")
data class DominionExpansion(
    @PrimaryKey var id: Int,
    var title: String
)
