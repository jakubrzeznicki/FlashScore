package com.kuba.flashscore.data.network.models.events

import com.kuba.flashscore.data.local.models.entities.event.StatisticEntity
import com.kuba.flashscore.data.local.models.entities.event.SubstitutionsEntity


data class Statistic(
    val away: String,
    val home: String,
    val type: String
) {
    fun asLocalModel(matchId: String): StatisticEntity {
        return StatisticEntity(
            matchId,
            away,
            home,
            type
        )
    }
}