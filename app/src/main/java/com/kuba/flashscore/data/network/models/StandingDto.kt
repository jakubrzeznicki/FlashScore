package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.local.models.entities.StandingType


data class StandingDto(
    @SerializedName("away_league_D")
    val awayLeagueD: String,
    @SerializedName("away_league_GA")
    val awayLeagueGA: String,
    @SerializedName("away_league_GF")
    val awayLeagueGF: String,
    @SerializedName("away_league_L")
    val awayLeagueL: String,
    @SerializedName("away_league_PTS")
    val awayLeaguePTS: String,
    @SerializedName("away_league_payed")
    val awayLeaguePayed: String,
    @SerializedName("away_league_position")
    val awayLeaguePosition: String,
    @SerializedName("away_league_W")
    val awayLeagueW: String,
    @SerializedName("away_promotion")
    val awayPromotion: String,
    @SerializedName("country_name")
    val countryName: String,
    @SerializedName("home_league_D")
    val homeLeagueD: String,
    @SerializedName("home_league_GA")
    val homeLeagueGA: String,
    @SerializedName("home_league_GF")
    val homeLeagueGF: String,
    @SerializedName("home_league_L")
    val homeLeagueL: String,
    @SerializedName("home_league_PTS")
    val homeLeaguePTS: String,
    @SerializedName("home_league_payed")
    val homeLeaguePayed: String,
    @SerializedName("home_league_position")
    val homeLeaguePosition: String,
    @SerializedName("home_league_W")
    val homeLeagueW: String,
    @SerializedName("home_promotion")
    val homePromotion: String,
    @SerializedName("league_id")
    val leagueId: String,
    @SerializedName("league_name")
    val leagueName: String,
    @SerializedName("league_round")
    val leagueRound: String,
    @SerializedName("overall_league_D")
    val overallLeagueD: String,
    @SerializedName("overall_league_GA")
    val overallLeagueGA: String,
    @SerializedName("overall_league_GF")
    val overallLeagueGF: String,
    @SerializedName("overall_league_L")
    val overallLeagueL: String,
    @SerializedName("overall_league_PTS")
    val overallLeaguePTS: String,
    @SerializedName("overall_league_payed")
    val overallLeaguePayed: String,
    @SerializedName("overall_league_position")
    val overallLeaguePosition: String,
    @SerializedName("overall_league_W")
    val overallLeagueW: String,
    @SerializedName("overall_promotion")
    val overallPromotion: String,
    @SerializedName("team_badge")
    val teamBadge: String,
    @SerializedName("team_id")
    val teamId: String,
    @SerializedName("team_name")
    val teamName: String
) {
    fun asLocalModelHome(): StandingEntity {
        return StandingEntity(
            leagueRound,
            homeLeagueD,
            homeLeagueGA,
            homeLeagueGF,
            homeLeagueL,
            homeLeaguePTS,
            homeLeaguePayed,
            homeLeaguePosition,
            homeLeagueW,
            homePromotion,
            teamId,
            leagueId,
            StandingType.HOME
        )
    }

    fun asLocalModelAway(): StandingEntity {
        return StandingEntity(
            leagueRound,
            awayLeagueD,
            awayLeagueGA,
            awayLeagueGF,
            awayLeagueL,
            awayLeaguePTS,
            awayLeaguePayed,
            awayLeaguePosition,
            awayLeagueW,
            awayPromotion,
            teamId,
            leagueId,
            StandingType.AWAY
        )
    }

    fun asLocalModelOverall(): StandingEntity {
        return StandingEntity(
            leagueRound,
            overallLeagueD,
            overallLeagueGA,
            overallLeagueGF,
            overallLeagueL,
            overallLeaguePTS,
            overallLeaguePayed,
            overallLeaguePosition,
            overallLeagueW,
            overallPromotion,
            teamId,
            leagueId,
            StandingType.OVERALL
        )
    }
}