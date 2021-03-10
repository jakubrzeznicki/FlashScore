package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Card
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "event_table"
)
data class EventEntity(
    @ColumnInfo(name = "league_id")
    val leagueId: String,
    @ColumnInfo(name = "match_date")
    val matchDate: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "match_id")
    val matchId: String,
    @ColumnInfo(name = "match_live")
    val matchLive: String,
    @ColumnInfo(name = "match_referee")
    val matchReferee: String,
    @ColumnInfo(name = "match_round")
    val matchRound: String,
    @ColumnInfo(name = "match_stadium")
    val matchStadium: String,
    @ColumnInfo(name = "match_status")
    val matchStatus: String,
    @ColumnInfo(name = "match_time")
    val matchTime: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel(): Event {
        return Event(
            leagueId,
            matchDate,
            matchId,
            matchLive,
            matchReferee,
            matchRound,
            matchStadium,
            matchStatus,
            matchTime
        )
    }
}