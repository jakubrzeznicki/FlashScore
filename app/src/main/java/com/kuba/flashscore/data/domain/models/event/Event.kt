package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val leagueId: String,
    val matchDate: String,
    val matchId: String,
    val matchLive: String,
    val matchReferee: String,
    val matchRound: String,
    val matchStadium: String,
    val matchStatus: String,
    val matchTime: String
) : Parcelable