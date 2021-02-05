package com.kuba.flashscore.network.models


import com.google.gson.annotations.SerializedName

data class TeamDto(
    val coaches: List<CoacheDto>,
    val players: List<PlayerDto>,
    @SerializedName("team_badge")
    val teamBadge: String,
    @SerializedName("team_key")
    val teamKey: String,
    @SerializedName("team_name")
    val teamName: String
)