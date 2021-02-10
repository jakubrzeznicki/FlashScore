package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalscorerDto(
    @SerializedName("away_assist")
    val awayAssist: String,
    @SerializedName("away_assist_id")
    val awayAssistId: String,
    @SerializedName("away_scorer")
    val awayScorer: String,
    @SerializedName("away_scorer_id")
    val awayScorerId: String,
    @SerializedName("home_assist")
    val homeAssist: String,
    @SerializedName("home_assist_id")
    val homeAssistId: String,
    @SerializedName("home_scorer")
    val homeScorer: String,
    @SerializedName("home_scorer_id")
    val homeScorerId: String,
    val info: String,
    val score: String,
    val time: String
) : Parcelable