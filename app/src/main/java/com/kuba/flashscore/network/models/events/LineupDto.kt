package com.kuba.flashscore.network.models.events


data class LineupDto(
    val awayDto: AwayDto,
    val homeDto: HomeDto
)