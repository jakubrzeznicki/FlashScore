package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.event.GoalscorerEntity

data class Goalscorer(
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
) {
    fun asLocalModel(matchId: String): GoalscorerEntity {
        var scorer = ""
        var assistantId = ""
        var whichTeam = false
        if (homeScorerId.isEmpty()) {
            scorer = awayScorerId
            assistantId = awayAssistId
            whichTeam = false
        } else {
            scorer = homeScorerId
            assistantId = homeAssistId
            whichTeam = true
        }
        return GoalscorerEntity(
            matchId,
            assistantId,
            scorer,
            whichTeam,
            info,
            score,
            time
        )
    }
}