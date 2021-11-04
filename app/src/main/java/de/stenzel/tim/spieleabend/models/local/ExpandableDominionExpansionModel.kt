package de.stenzel.tim.spieleabend.models.local

class ExpandableDominionExpansionModel(
    val expansion: DominionModel.Expansion,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = true
)