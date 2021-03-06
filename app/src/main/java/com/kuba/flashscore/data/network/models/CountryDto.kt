package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("country_logo")
    val countryLogo: String,
    @SerializedName("country_name")
    val countryName: String
)