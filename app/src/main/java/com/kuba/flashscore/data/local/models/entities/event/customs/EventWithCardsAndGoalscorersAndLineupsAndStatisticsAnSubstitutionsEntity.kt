package com.kuba.flashscore.data.local.models.entities.event.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.data.domain.models.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.data.local.models.entities.event.*

data class EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
    @Embedded
    val eventEntity: EventEntity,

    @Relation(parentColumn = "match_id", entityColumn = "card_match_id", entity = CardEntity::class)
    val cards: List<CardEntity> = emptyList(),

    @Relation(
        parentColumn = "match_id",
        entityColumn = "goalscorer_match_id",
        entity = GoalscorerEntity::class
    )
    val goalscorers: List<GoalscorerEntity> = emptyList(),

    @Relation(
        parentColumn = "match_id",
        entityColumn = "lineup_match_id",
        entity = LineupEntity::class
    )
    val lineups: List<LineupEntity> = emptyList(),

    @Relation(
        parentColumn = "match_id",
        entityColumn = "statistic_match_id",
        entity = StatisticEntity::class
    )
    val statistics: List<StatisticEntity> = emptyList(),

    @Relation(
        parentColumn = "match_id",
        entityColumn = "substitutions_match_id",
        entity = SubstitutionsEntity::class
    )
    val substitutions: List<SubstitutionsEntity> = emptyList()
) {
    fun asDomainModel(): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions {
        return EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions(
            eventEntity.asDomainModel(),
            cards.map { it.asDomainModel() },
            goalscorers.map { it.asDomainModel() },
            lineups.map { it.asDomainModel() },
            statistics.map { it.asDomainModel() },
            substitutions.map { it.asDomainModel() }
        )
    }
}