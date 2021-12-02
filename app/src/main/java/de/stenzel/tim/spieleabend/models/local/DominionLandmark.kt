package de.stenzel.tim.spieleabend.models.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dominion_landmarks",
    foreignKeys = [ForeignKey(entity = DominionExpansion::class,
        parentColumns = ["id"], childColumns = ["expansionId"])])
data class DominionLandmark(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var price: Int,
    var text: String,
    var expansionId: Int
) : Parcelable