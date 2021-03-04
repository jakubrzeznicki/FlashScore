package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
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

