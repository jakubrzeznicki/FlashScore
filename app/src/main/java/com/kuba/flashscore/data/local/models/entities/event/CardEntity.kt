package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Card
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "card_table", primaryKeys = ["time", "fault", "card_match_id"])
data class CardEntity(
    @ColumnInfo(name = "card_match_id")
    val matchId: String,
    val fault: String,
    val card: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val info: String,
    val time: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel(): Card {
        return Card(
            matchId,
            fault,
            card,
            whichTeam,
            info,
            time
        )
    }
}