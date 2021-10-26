package de.stenzel.tim.spieleabend.models

class ExpandableDominionCardModel(
    val card: DominionModel.Expansion.Card,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = true
)