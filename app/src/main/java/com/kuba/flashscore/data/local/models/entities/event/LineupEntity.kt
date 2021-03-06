package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "lineup_table", primaryKeys = ["lineup_number", "lineup_match_id", "player_id"])
data class LineupEntity(
    @ColumnInfo(name = "lineup_match_id")
    val matchId: String,
    @ColumnInfo(name = "lineup_number")
    val lineupNumber: String,
    @ColumnInfo(name = "lineup_position")
    val lineupPosition: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val starting: Boolean,
    val missing: Boolean,
    @ColumnInfo(name = "player_id")
    val playerKey: String
) : Parcelable

