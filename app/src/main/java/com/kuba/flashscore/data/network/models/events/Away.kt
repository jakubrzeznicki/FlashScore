package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName

data class Away(
    @SerializedName("coach")
    val coaches: List<Coach>,
    @SerializedName("missing_players")
    val missingPlayers: List<MissingPlayer>,
    @SerializedName("starting_lineups")
    val startingLineups: List<StartingLineup>,
    val substitutes: List<Substitute>
)