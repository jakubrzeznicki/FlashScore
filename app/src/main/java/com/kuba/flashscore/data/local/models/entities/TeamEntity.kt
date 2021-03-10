package com.kuba.flashscore.data.local.models.entities

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.Team
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "team_table")
data class TeamEntity(
    @ColumnInfo(name = "league_id")
    val leagueId: String?,
    @ColumnInfo(name = "team_badge")
    val teamBadge: String,
    @ColumnInfo(name = "team_id")
    @PrimaryKey(autoGenerate = false)
    val teamKey: String,
    @ColumnInfo(name = "team_name")
    val teamName: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel() : Team  {
        return Team(
            leagueId,
            teamBadge,
            teamKey,
            teamName
        )
    }
}