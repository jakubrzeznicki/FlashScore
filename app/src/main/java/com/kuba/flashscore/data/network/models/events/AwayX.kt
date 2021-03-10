package com.kuba.flashscore.data.network.models.events

import com.kuba.flashscore.data.local.models.entities.event.SubstitutionsEntity


data class AwayX(
    val substitution: String,
    val time: String
) {
    fun asLocalModel(matchId: String): SubstitutionsEntity {
        return SubstitutionsEntity(
            matchId,
            substitution,
            time,
            false
        )
    }
}