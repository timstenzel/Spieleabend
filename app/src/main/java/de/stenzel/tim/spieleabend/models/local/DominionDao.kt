package de.stenzel.tim.spieleabend.models.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DominionDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExpansions(expansions: List<DominionExpansion>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCards(cards: List<DominionCard>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(events: List<DominionEvent>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLandmarks(landmarks: List<DominionLandmark>)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPriceCurves(priceCurves: List<DominionPriceCurve>)

    @Query("SELECT * FROM dominion_expansions")
    suspend fun getExpansions() : List<DominionExpansion>

    @Query("SELECT * FROM dominion_cards")
    suspend fun getCards() : List<DominionCard>

    @Query("SELECT * FROM dominion_events")
    suspend fun getEvents() : List<DominionEvent>

    @Query("SELECT * FROM dominion_landmarks")
    suspend fun getLandmarks() : List<DominionLandmark>

    @Query("SELECT * FROM dominion_price_curves")
    suspend fun getPriceCurves() : List<DominionPriceCurve>

}