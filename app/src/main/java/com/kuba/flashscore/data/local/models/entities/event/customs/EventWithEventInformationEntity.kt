package com.kuba.flashscore.data.local.models.entities.event.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.domain.models.event.customs.EventWithEventInformation
import com.kuba.flashscore.data.local.models.entities.event.EventEntity
import com.kuba.flashscore.data.local.models.entities.event.EventInformationEntity

data class EventWithEventInformationEntity(
    @Embedded
    val event: EventEntity,

    @Relation(parentColumn = "match_id", entityColumn = "match_id", entity = EventInformationEntity::class)
    val eventInformation: List<EventInformationEntity> = emptyList(),
) {
    fun asDomainModel(): EventWithEventInformation {
        return EventWithEventInformation(
            event.asDomainModel(),
            eventInformation.map { it.asDomainModel() }
        )
    }
}