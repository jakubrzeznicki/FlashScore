package com.kuba.flashscore.data.network.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerInfoDto(
    @SerializedName("player_age")
    val playerAge: String,
    @SerializedName("player_country")
    val playerCountry: String,
    @SerializedName("player_goals")
    val playerGoals: String,
    @SerializedName("player_key")
    val playerKey: Long,
    @SerializedName("player_match_played")
    val playerMatchPlayed: String,
    @SerializedName("player_name")
    val playerName: String,
    @SerializedName("player_number")
    val playerNumber: String,
    @SerializedName("player_red_cards")
    val playerRedCards: String,
    @SerializedName("player_type")
    val playerType: String,
    @SerializedName("player_yellow_cards")
    val playerYellowCards: String,
    @SerializedName("team_key")
    val teamKey: String,
    @SerializedName("team_name")
    val teamName: String
) : Parcelable