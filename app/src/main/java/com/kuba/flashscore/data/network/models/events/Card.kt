package com.kuba.flashscore.data.network.models.events


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.local.models.entities.event.CardEntity
import kotlin.reflect.typeOf


data class Card(
    @SerializedName("away_fault")
    val awayFault: String,
    val card: String,
    @SerializedName("home_fault")
    val homeFault: String,
    val info: String,
    val time: String
) {
    fun asLocalModel(matchId: String): CardEntity {
        return CardEntity(
            matchId,
            if (awayFault.isEmpty()) homeFault else awayFault,
            card,
            awayFault.isEmpty(),
            info,
            time
        )
    }
}