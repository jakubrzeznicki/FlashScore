package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class EventDto(
    val cards: List<Card>,
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("country_logo")
    val countryLogo: String,
    @SerializedName("country_name")
    val countryName: String,
    val goalscorer: List<Goalscorer>,
    @SerializedName("league_id")
    val leagueId: String,
    @SerializedName("league_logo")
    val leagueLogo: String,
    @SerializedName("league_name")
    val leagueName: String,
    val lineup: Lineup,
    @SerializedName("match_awayteam_extra_score")
    val matchAwayteamExtraScore: String,
    @SerializedName("match_awayteam_ft_score")
    val matchAwayteamFtScore: String,
    @SerializedName("match_awayteam_halftime_score")
    val matchAwayteamHalftimeScore: String,
    @SerializedName("match_awayteam_id")
    val matchAwayteamId: String,
    @SerializedName("match_awayteam_name")
    val matchAwayteamName: String,
    @SerializedName("match_awayteam_penalty_score")
    val matchAwayteamPenaltyScore: String,
    @SerializedName("match_awayteam_score")
    val matchAwayteamScore: String,
    @SerializedName("match_awayteam_system")
    val matchAwayteamSystem: String,
    @SerializedName("match_date")
    val matchDate: String,
    @SerializedName("match_hometeam_extra_score")
    val matchHometeamExtraScore: String,
    @SerializedName("match_hometeam_ft_score")
    val matchHometeamFtScore: String,
    @SerializedName("match_hometeam_halftime_score")
    val matchHometeamHalftimeScore: String,
    @SerializedName("match_hometeam_id")
    val matchHometeamId: String,
    @SerializedName("match_hometeam_name")
    val matchHometeamName: String,
    @SerializedName("match_hometeam_penalty_score")
    val matchHometeamPenaltyScore: String,
    @SerializedName("match_hometeam_score")
    val matchHometeamScore: String,
    @SerializedName("match_hometeam_system")
    val matchHometeamSystem: String,
    @SerializedName("match_id")
    val matchId: String,
    @SerializedName("match_live")
    val matchLive: String,
    @SerializedName("match_referee")
    val matchReferee: String,
    @SerializedName("match_round")
    val matchRound: String,
    @SerializedName("match_stadium")
    val matchStadium: String,
    @SerializedName("match_status")
    val matchStatus: String,
    @SerializedName("match_time")
    val matchTime: String,
    val statistics: List<Statistic>,
    val substitutions: Substitutions,
    @SerializedName("team_away_badge")
    val teamAwayBadge: String,
    @SerializedName("team_home_badge")
    val teamHomeBadge: String
)