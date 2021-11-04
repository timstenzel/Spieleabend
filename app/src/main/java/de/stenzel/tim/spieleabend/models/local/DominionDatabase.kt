package de.stenzel.tim.spieleabend.models.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DominionExpansion::class, DominionCard::class,
        DominionEvent::class, DominionLandmark::class, DominionPriceCurve::class],
    version = 1
)
abstract class DominionDatabase : RoomDatabase() {

    abstract fun dominionDao(): DominionDao
}