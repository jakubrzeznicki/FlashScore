package com.kuba.flashscore.data.network.models.events

import com.google.gson.annotations.SerializedName


data class Lineup(
    @SerializedName("away")
    val away: Away,
    @SerializedName("home")
    val home: Home
)