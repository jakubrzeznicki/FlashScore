package com.kuba.flashscore.repositories.event

import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface EventRepository {
    suspend fun insertCards(cards: List<CardEntity>)
    suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>)
    suspend fun insertLineups(lineups: List<LineupEntity>)
    suspend fun insertStatistics(statistics: List<StatisticEntity>)
    suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>)
    suspend fun insertEvents(events: List<EventEntity>)
    suspend fun getEventsFromSpecificLeaguesFromDb(leagueId: String, date: String): List<EventEntity>
    suspend fun getEventsFromSpecificLeaguesFromNetwork(
        leagueId: String,
        from: String,
        to: String
    ): Resource<List<EventEntity>>
}
