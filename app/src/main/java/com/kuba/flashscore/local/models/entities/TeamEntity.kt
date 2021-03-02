package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.network.models.CoacheDto
import com.kuba.flashscore.network.models.PlayerDto
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
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