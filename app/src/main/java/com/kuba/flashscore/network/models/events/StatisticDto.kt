package com.kuba.flashscore.network.models.events


import com.google.gson.annotations.SerializedName

data class StatisticDto(
    val away: String,
    val home: String,
    val type: String
)