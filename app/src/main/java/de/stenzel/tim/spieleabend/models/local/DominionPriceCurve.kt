package de.stenzel.tim.spieleabend.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dominion_price_curves")
data class DominionPriceCurve(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val twoOrBelow : Int,
    val three : Int,
    val four : Int,
    val five : Int,
    val sixOrAbove : Int
)
