package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName

data class CoachX(
    @SerializedName("lineup_number")
    val lineupNumber: String,
    @SerializedName("lineup_player")
    val lineupPlayer: String,
    @SerializedName("lineup_position")
    val lineupPosition: String,
    @SerializedName("player_key")
    val playerKey: String
)