package com.kuba.flashscore.data.local.models.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val teamName: String
) : Parcelable