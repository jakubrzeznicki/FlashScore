package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val leagueId: String,
    val matchAwayteamExtraScore: String,
    val matchAwayteamFtScore: String,
    val matchAwayteamHalftimeScore: String,
    val matchAwayteamId: String,
    val matchAwayteamPenaltyScore: String,
    val matchAwayteamScore: String,
    val matchAwayteamSystem: String,
    val matchDate: String,
    val matchHometeamExtraScore: String,
    val matchHometeamFtScore: String,
    val matchHometeamHalftimeScore: String,
    val matchHometeamId: String,
    val matchHometeamPenaltyScore: String,
    val matchHometeamScore: String,
    val matchHometeamSystem: String,
    val matchId: String,
    val matchLive: String,
    val matchReferee: String,
    val matchRound: String,
    val matchStadium: String,
    val matchStatus: String,
    val matchTime: String
) : Parcelable