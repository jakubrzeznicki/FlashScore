package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName


data class Card(
    @SerializedName("away_fault")
    val awayFault: String,
    val card: String,
    @SerializedName("home_fault")
    val homeFault: String,
    val info: String,
    val time: String
)