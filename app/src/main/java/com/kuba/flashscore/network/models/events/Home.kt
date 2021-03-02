package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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