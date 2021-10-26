package de.stenzel.tim.spieleabend.models

data class DominionPriceCurve(
    val twoOrBelow : Int,
    val three : Int,
    val four : Int,
    val five : Int,
    val sixOrAbove : Int
)
