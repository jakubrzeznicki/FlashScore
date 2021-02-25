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
@Entity(tableName = "standing_table", primaryKeys = ["team_id","league_id"])
data class StandingEntity(
    @ColumnInfo(name = "away_league_D")
    val awayLeagueD: String,
    @ColumnInfo(name = "away_league_GA")
    val awayLeagueGA: String,
    @ColumnInfo(name = "away_league_GF")
    val awayLeagueGF: String,
    @ColumnInfo(name = "away_league_L")
    val awayLeagueL: String,
    @ColumnInfo(name = "away_league_PTS")
    val awayLeaguePTS: String,
    @ColumnInfo(name = "away_league_payed")
    val awayLeaguePayed: String,
    @ColumnInfo(name = "away_league_position")
    val awayLeaguePosition: String,
    @ColumnInfo(name = "away_league_W")
    val awayLeagueW: String,
    @ColumnInfo(name = "away_promotion")
    val awayPromotion: String,
    @ColumnInfo(name = "home_league_D")
    val homeLeagueD: String,
    @ColumnInfo(name = "home_league_GA")
    val homeLeagueGA: String,
    @ColumnInfo(name = "home_league_GF")
    val homeLeagueGF: String,
    @ColumnInfo(name = "home_league_L")
    val homeLeagueL: String,
    @ColumnInfo(name = "home_league_PTS")
    val homeLeaguePTS: String,
    @ColumnInfo(name = "home_league_payed")
    val homeLeaguePayed: String,
    @ColumnInfo(name = "home_league_position")
    val homeLeaguePosition: String,
    @ColumnInfo(name = "home_league_W")
    val homeLeagueW: String,
    @ColumnInfo(name = "home_promotion")
    val homePromotion: String,
    @SerializedName("league_round")
    val leagueRound: String,
    @ColumnInfo(name = "overall_league_D")
    val overallLeagueD: String,
    @ColumnInfo(name = "overall_league_GA")
    val overallLeagueGA: String,
    @ColumnInfo(name = "overall_league_GF")
    val overallLeagueGF: String,
    @ColumnInfo(name = "overall_league_L")
    val overallLeagueL: String,
    @ColumnInfo(name = "overall_league_PTS")
    val overallLeaguePTS: String,
    @ColumnInfo(name = "overall_league_payed")
    val overallLeaguePayed: String,
    @ColumnInfo(name = "overall_league_position")
    val overallLeaguePosition: String,
    @ColumnInfo(name = "overall_league_W")
    val overallLeagueW: String,
    @ColumnInfo(name = "overall_promotion")
    val overallPromotion: String,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "league_id")
    val leagueId: String

) : Parcelable