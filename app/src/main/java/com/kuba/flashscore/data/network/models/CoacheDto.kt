package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName

data class CoacheDto(
    @SerializedName("coach_age")
    val coachAge: String,
    @SerializedName("coach_country")
    val coachCountry: String,
    @SerializedName("coach_name")
    val coachName: String
)