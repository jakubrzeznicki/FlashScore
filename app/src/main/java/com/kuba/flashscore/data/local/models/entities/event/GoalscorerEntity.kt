package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Goalscorer
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "goalscorer_table", primaryKeys = ["time", "scorer_id", "goalscorer_match_id"])
data class GoalscorerEntity(
    @ColumnInfo(name = "goalscorer_match_id")
    val matchId: String,
    @ColumnInfo(name = "assist_id")
    val assistId: String,
    @ColumnInfo(name = "scorer_id")
    val scorerId: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val info: String,
    val score: String,
    val time: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel() :Goalscorer {
        return Goalscorer(
            matchId,
            assistId,
            scorerId,
            whichTeam,
            info,
            score,
            time
        )
    }
}