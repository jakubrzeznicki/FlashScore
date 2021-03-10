package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.event.LineupEntity

data class Substitute(
    @SerializedName("lineup_number")
    val lineupNumber: String,
    @SerializedName("lineup_player")
    val lineupPlayer: String,
    @SerializedName("lineup_position")
    val lineupPosition: String,
    @SerializedName("player_key")
    val playerKey: String
) {
    fun asLocalModel(matchId: String): LineupEntity {
        return LineupEntity(
            matchId,
            lineupNumber,
            lineupPosition,
            true,
            starting = false,
            missing = false,
            playerKey = playerKey
        )
    }
}