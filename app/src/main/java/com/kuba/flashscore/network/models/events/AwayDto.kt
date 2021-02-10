package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AwayDto(
    val coachDtos: List<CoachDto>,
    @SerializedName("missing_players")
    val missingPlayerDtos: List<MissingPlayerDto>,
    @SerializedName("starting_lineups")
    val startingLineupDtos: List<StartingLineupDto>,
    val substituteDtos: List<SubstituteDto>
) : Parcelable