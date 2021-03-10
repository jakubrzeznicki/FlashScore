package com.kuba.flashscore.data.local.models.entities.event.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.domain.models.event.customs.LeagueWithEvents
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.event.EventEntity

data class LeagueWithEventsEntity(
    @Embedded
    val league: LeagueEntity,

    @Relation(parentColumn = "league_id", entityColumn = "league_id", entity = EventEntity::class)
    val eventsWithEventsInformation: List<EventWithEventInformationEntity?> = emptyList()
) {
    fun asDomainModel(): LeagueWithEvents {
        return LeagueWithEvents(
            league.asDomainModel(),
            eventsWithEventsInformation.map { it?.asDomainModel()!! }
        )
    }
}