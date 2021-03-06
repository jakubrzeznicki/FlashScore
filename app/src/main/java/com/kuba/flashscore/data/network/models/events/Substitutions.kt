package com.kuba.flashscore.data.network.models.events


data class Substitutions(
    val away: List<AwayX>,
    val home: List<HomeX>
)