package de.stenzel.tim.spieleabend.repositories

import de.stenzel.tim.spieleabend.models.local.*
import javax.inject.Inject

class DefaultDominionRepository @Inject constructor(
    private val dominionDao: DominionDao
) : DominionRepository {

    override suspend fun insertExpansions(expansions: List<DominionExpansion>) {
        dominionDao.insertAllExpansions(expansions)
    }

    override suspend fun insertCards(cards: List<DominionCard>) {
        dominionDao.insertAllCards(cards)
    }

    override suspend fun insertEvents(events: List<DominionEvent>) {
        dominionDao.insertAllEvents(events)
    }

    override suspend fun insertLandmarks(landmarks: List<DominionLandmark>) {
        dominionDao.insertAllLandmarks(landmarks)
    }

    override suspend fun insertPriceCurves(curves: List<DominionPriceCurve>) {
        dominionDao.insertAllPriceCurves(curves)
    }


    override suspend fun getExpansions(): List<DominionExpansion> {
        return dominionDao.getExpansions()
    }

    override suspend fun getCards(): List<DominionCard> {
        return dominionDao.getCards()
    }

    override suspend fun getPriceCurves(): List<DominionPriceCurve> {
        return dominionDao.getPriceCurves()
    }

    override suspend fun getEvents(): List<DominionEvent> {
        return dominionDao.getEvents()
    }

    override suspend fun getLandmarks(): List<DominionLandmark> {
        return dominionDao.getLandmarks()
    }
}