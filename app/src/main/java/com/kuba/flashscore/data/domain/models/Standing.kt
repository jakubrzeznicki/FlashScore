package com.kuba.flashscore.data.domain.models

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.StandingType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Standing(
    val leagueRound: String,
    val leagueD: String,
    val leagueGA: String,
    val leagueGF: String,
    val leagueL: String,
    val leaguePTS: String,
    val leaguePayed: String,
    val leaguePosition: String,
    val leagueW: String,
    val promotion: String,
    val teamId: String,
    val leagueId: String,
    val standingType: StandingType
) : Parcelable