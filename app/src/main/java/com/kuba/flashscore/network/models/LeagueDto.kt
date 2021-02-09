package com.kuba.flashscore.network.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueDto(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("country_logo")
    val countryLogo: String,
    @SerializedName("country_name")
    val countryName: String,
    @SerializedName("league_id")
    val leagueId: String,
    @SerializedName("league_logo")
    val leagueLogo: String,
    @SerializedName("league_name")
    val leagueName: String,
    @SerializedName("league_season")
    val leagueSeason: String
) : Parcelable