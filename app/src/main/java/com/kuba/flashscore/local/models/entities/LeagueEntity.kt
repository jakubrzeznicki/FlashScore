package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "leagues_table"
)
data class LeagueEntity(
    @ColumnInfo(name = "league_country_id")
    val countryId: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "league_id")
    val leagueId: String,
    @ColumnInfo(name = "league_logo")
    val leagueLogo: String,
    @ColumnInfo(name = "league_name")
    val leagueName: String,
    @ColumnInfo(name = "league_season")
    val leagueSeason: String
) : Parcelable