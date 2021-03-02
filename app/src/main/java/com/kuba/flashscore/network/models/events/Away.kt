package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Away(
    @SerializedName("coach")
    val coaches: List<Coach>,
    @SerializedName("missing_players")
    val missingPlayers: List<MissingPlayer>,
    @SerializedName("starting_lineups")
    val startingLineups: List<StartingLineup>,
    val substitutes: List<Substitute>
)