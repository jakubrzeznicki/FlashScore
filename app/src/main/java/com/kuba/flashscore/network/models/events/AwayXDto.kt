package com.kuba.flashscore.network.models.events


import com.google.gson.annotations.SerializedName

data class AwayXDto(
    val substitution: String,
    val time: String
)