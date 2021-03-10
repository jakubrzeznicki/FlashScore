package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.LeagueEntity

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
) {
    fun asLocalModel() : LeagueEntity {
        return LeagueEntity(
            countryId,
            leagueId,
            leagueLogo,
            leagueName,
            leagueSeason
        )
    }
}