package com.kuba.flashscore.network.models.events


data class SubstitutionsDto(
    val awayDto: List<AwayXDto>,
    val home: List<HomeXDto>
)