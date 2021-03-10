package com.kuba.flashscore.repositories.event

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.other.Resource

interface EventRepository {
    suspend fun insertCards(cards: List<CardEntity>)
    suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>)
    suspend fun insertLineups(lineups: List<LineupEntity>)
    suspend fun insertStatistics(statistics: List<StatisticEntity>)
    suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>)
    suspend fun insertEvents(events: List<EventEntity>)
    suspend fun insertEventInformation(eventInformation: List<EventInformationEntity>)
    suspend fun getEventsFromSpecificLeaguesFromDb(
        leagueId: String,
        date: String
    ): List<EventEntity>

    suspend fun refreshEventsFromSpecificLeagues(
        leagueId: String,
        date: String
    ): Resource<List<Event>>

    suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeamsEntity

    suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
}
