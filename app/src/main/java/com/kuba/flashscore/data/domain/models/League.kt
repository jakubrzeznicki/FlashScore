package com.kuba.flashscore.data.domain.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class League(
    val countryId: String,
    val leagueId: String,
    val leagueLogo: String,
    val leagueName: String,
    val leagueSeason: String
) : Parcelable