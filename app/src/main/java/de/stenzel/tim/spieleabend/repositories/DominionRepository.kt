package de.stenzel.tim.spieleabend.repositories

import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.DominionModel
import de.stenzel.tim.spieleabend.models.DominionPriceCurve

interface DominionRepository {

    fun getDominionExpansionsWithCards() : Resource<DominionModel>

    fun getPriceCurve() : DominionPriceCurve

    fun getRandomEvent() : DominionModel.Expansion.Card

    fun getRandomMonument() : DominionModel.Expansion.Card
}