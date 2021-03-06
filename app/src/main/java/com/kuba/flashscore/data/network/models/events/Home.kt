package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName

data class Home(
    @SerializedName("coach")
    val coaches: List<CoachX>,
    @SerializedName("missing_players")
    val missingPlayers: List<MissingPlayerX>,
    @SerializedName("starting_lineups")
    val startingLineups: List<StartingLineupX>,
    @SerializedName("substitute")
    val substitutes: List<SubstituteX>
)