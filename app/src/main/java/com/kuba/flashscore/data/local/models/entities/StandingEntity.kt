package com.kuba.flashscore.data.local.models.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.other.DateUtils

@Entity(tableName = "standing_table", primaryKeys = ["team_id", "league_id", "standing_type"])
data class StandingEntity(
    @SerializedName("league_round")
    val leagueRound: String,
    @ColumnInfo(name = "league_D")
    val leagueD: String,
    @ColumnInfo(name = "league_GA")
    val leagueGA: String,
    @ColumnInfo(name = "overall_league_GF")
    val leagueGF: String,
    @ColumnInfo(name = "overall_league_L")
    val leagueL: String,
    @ColumnInfo(name = "overall_league_PTS")
    val leaguePTS: String,
    @ColumnInfo(name = "overall_league_payed")
    val leaguePayed: String,
    @ColumnInfo(name = "overall_league_position")
    val leaguePosition: String,
    @ColumnInfo(name = "overall_league_W")
    val leagueW: String,
    @ColumnInfo(name = "overall_promotion")
    val promotion: String,
    @ColumnInfo(name = "team_id")
    val teamId: String,
    @ColumnInfo(name = "league_id")
    val leagueId: String,
    @ColumnInfo(name = "standing_type")
    val standingType: StandingType,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel(): Standing {
        return Standing(
            leagueRound,
            leagueD,
            leagueGA,
            leagueGF,
            leagueL,
            leaguePTS,
            leaguePayed,
            leaguePosition,
            leagueW,
            promotion,
            teamId,
            leagueId,
            standingType
        )
    }
}

enum class StandingType {
    AWAY,
    HOME,
    OVERALL
}