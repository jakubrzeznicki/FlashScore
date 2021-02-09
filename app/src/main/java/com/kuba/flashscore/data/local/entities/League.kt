package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class League(
    @PrimaryKey(autoGenerate = false)
    val leagueId: String,
    val leagueLogo: String,
    val leagueName: String,
    val leagueSeason: String,
    val countryId: String,
    val countryName: String,
    val countryLogo: String
) : Parcelable