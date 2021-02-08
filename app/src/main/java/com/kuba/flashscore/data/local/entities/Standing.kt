package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Standing(
    val awayLeagueD: String,
    val awayLeagueGA: String,
    val awayLeagueGF: String,
    val awayLeagueL: String,
    val awayLeaguePTS: String,
    val awayLeaguePayed: String,
    val awayLeaguePosition: String,
    val awayLeagueW: String,
    val awayPromotion: String,
    val countryName: String,
    val homeLeagueD: String,
    val homeLeagueGA: String,
    val homeLeagueGF: String,
    val homeLeagueL: String,
    val homeLeaguePTS: String,
    val homeLeaguePayed: String,
    val homeLeaguePosition: String,
    val homeLeagueW: String,
    val homePromotion: String,
    val leagueId: String,
    val leagueName: String,
    val leagueRound: String,
    val overallLeagueD: String,
    val overallLeagueGA: String,
    val overallLeagueGF: String,
    val overallLeagueL: String,
    val overallLeaguePTS: String,
    val overallLeaguePayed: String,
    val overallLeaguePosition: String,
    val overallLeagueW: String,
    val overallPromotion: String,
    val teamBadge: String,
    val teamId: String,
    val teamName: String
) : Parcelable