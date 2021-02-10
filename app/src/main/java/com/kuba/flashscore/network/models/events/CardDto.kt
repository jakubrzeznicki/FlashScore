package com.kuba.flashscore.network.models.events


import com.google.gson.annotations.SerializedName

data class CardDto(
    @SerializedName("away_fault")
    val awayFault: String,
    val card: String,
    @SerializedName("home_fault")
    val homeFault: String,
    val info: String,
    val time: String
)