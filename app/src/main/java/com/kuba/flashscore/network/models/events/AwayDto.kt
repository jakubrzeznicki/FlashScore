package com.kuba.flashscore.network.models.events


import com.google.gson.annotations.SerializedName

data class AwayDto(
    val coachDtos: List<CoachDto>,
    @SerializedName("missing_players")
    val missingPlayerDtos: List<MissingPlayerDto>,
    @SerializedName("starting_lineups")
    val startingLineupDtos: List<StartingLineupDto>,
    val substituteDtos: List<SubstituteDto>
)