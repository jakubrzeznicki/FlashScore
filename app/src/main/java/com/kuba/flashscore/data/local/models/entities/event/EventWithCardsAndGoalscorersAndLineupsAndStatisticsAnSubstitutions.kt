package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions(
    @Embedded
    val eventEntity: EventEntity,

    @Relation(parentColumn = "match_id", entityColumn = "card_match_id", entity = CardEntity::class)
    val cards: List<CardEntity> = emptyList(),

    @Relation(parentColumn = "match_id", entityColumn = "goalscorer_match_id", entity = GoalscorerEntity::class)
    val goalscorers: List<GoalscorerEntity> = emptyList(),

    @Relation(parentColumn = "match_id", entityColumn = "lineup_match_id", entity = LineupEntity::class)
    val lineups: List<LineupEntity> = emptyList(),

    @Relation(parentColumn = "match_id", entityColumn = "statistic_match_id", entity = StatisticEntity::class)
    val statistics: List<StatisticEntity> = emptyList(),

    @Relation(parentColumn = "match_id", entityColumn = "substitutions_match_id", entity = SubstitutionsEntity::class)
    val substitutions: List<SubstitutionsEntity> = emptyList(),

    ) : Parcelable