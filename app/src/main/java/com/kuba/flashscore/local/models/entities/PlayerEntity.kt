package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "player_table")
data class PlayerEntity(
    @ColumnInfo(name = "player_team_id")
    val teamId: String?,
    @ColumnInfo(name = "player_age")
    val playerAge: String,
    @ColumnInfo(name ="player_country")
    val playerCountry: String,
    @ColumnInfo(name ="player_goals")
    val playerGoals: String,
    @ColumnInfo(name ="player_id")
    @PrimaryKey(autoGenerate = false)
    val playerKey: Long,
    @ColumnInfo(name ="player_match_played")
    val playerMatchPlayed: String,
    @ColumnInfo(name ="player_name")
    val playerName: String,
    @ColumnInfo(name ="player_number")
    val playerNumber: String,
    @ColumnInfo(name ="player_red_cards")
    val playerRedCards: String,
    @ColumnInfo(name ="player_type")
    val playerType: String,
    @ColumnInfo(name ="player_yellow_cards")
    val playerYellowCards: String
) : Parcelable