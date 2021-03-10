package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.TeamEntity

data class TeamDto(
    val coaches: List<CoacheDto>,
    val players: List<PlayerDto>,
    @SerializedName("team_badge")
    val teamBadge: String,
    @SerializedName("team_key")
    val teamKey: String,
    @SerializedName("team_name")
    val teamName: String
) {
    fun asLocalModel(leagueId: String): TeamEntity {
        return TeamEntity(
            leagueId,
            teamBadge,
            teamKey,
            teamName
        )
    }
}