package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.local.*

interface DominionRepository {

    suspend fun insertExpansions(expansions: List<DominionExpansion>)

    suspend fun insertCards(cards: List<DominionCard>)

    suspend fun insertEvents(events: List<DominionEvent>)

    suspend fun insertLandmarks(landmarks: List<DominionLandmark>)

    suspend fun insertPriceCurves(curves: List<DominionPriceCurve>)

    suspend fun getExpansions() : List<DominionExpansion>

    suspend fun getCards() : List<DominionCard>

    suspend fun getPriceCurves() : List<DominionPriceCurve>

    suspend fun getEvents() : List<DominionEvent>

    suspend fun getLandmarks() : List<DominionLandmark>
}