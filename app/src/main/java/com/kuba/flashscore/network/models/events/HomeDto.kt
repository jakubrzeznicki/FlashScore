package com.kuba.flashscore.network.models.events


import com.google.gson.annotations.SerializedName

data class HomeDto(
    val coachDtos: List<CoachXDto>,
    @SerializedName("missing_players")
    val missingPlayerDtos: List<MissingPlayerXDto>,
    @SerializedName("starting_lineups")
    val startingLineupDtos: List<StartingLineupXDto>,
    val substituteDtos: List<SubstituteXDto>
)