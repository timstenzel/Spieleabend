package de.stenzel.tim.spieleabend.models.local

class ExpandableDominionCardModel(
    val card: DominionModel.Expansion.Card,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = true
)