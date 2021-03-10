package com.kuba.flashscore.data.local.models.entities.event

import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Card
import com.kuba.flashscore.data.domain.models.event.EventInformation
import com.kuba.flashscore.other.DateUtils

@Entity(tableName = "event_information_table", primaryKeys = ["match_id", "team_id", "is_home"])
data class EventInformationEntity(
    @ColumnInfo(name = "match_id")
    val matchId: String,
    @ColumnInfo(name = "extra_score")
    val extraScore: String,
    @ColumnInfo(name = "full_time_score")
    val fullTImeScore: String,
    @ColumnInfo(name = "halftime_score")
    val halftimeScore: String,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "team_penalty_score")
    val teamPenaltyScore: String,
    @ColumnInfo(name = "team_score")
    val teamScore: String,
    @ColumnInfo(name = "team_system")
    val teamSystem: String,
    @ColumnInfo(name = "is_home")
    val isHome: Boolean,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel(): EventInformation {
        return EventInformation(
            matchId,
            extraScore,
            fullTImeScore,
            halftimeScore,
            teamId,
            teamPenaltyScore,
            teamScore,
            teamSystem,
            isHome
        )
    }
}