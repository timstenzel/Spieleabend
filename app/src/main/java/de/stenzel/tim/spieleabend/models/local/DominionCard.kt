package de.stenzel.tim.spieleabend.models.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dominion_cards",
    foreignKeys = [ForeignKey(entity = DominionExpansion::class,
        parentColumns = ["id"], childColumns = ["expansionId"])])
data class DominionCard(
    @PrimaryKey var id: Int,
    var title: String,
    var price: Int,
    var text: String,
    var expansionId: Int
) : Parcelable
