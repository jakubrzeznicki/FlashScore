package com.kuba.flashscore.repositories

import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.event.EventRepository
import com.kuba.flashscore.repositories.league.LeagueRepository
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceEventEntity
import com.kuba.flashscore.util.DataProducer.produceEventInformationEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity

class FakeEventRepositoryAndroidTest : EventRepository {

    private val cardItems = mutableListOf<CardEntity>()
    private val goalscorerItems = mutableListOf<GoalscorerEntity>()
    private val lineupItems = mutableListOf<LineupEntity>()
    private val statisticsItems = mutableListOf<StatisticEntity>()
    private val substitutionsItems = mutableListOf<SubstitutionsEntity>()
    private val eventItems = mutableListOf<EventEntity>()
    private val eventInformationItems = mutableListOf<EventInformationEntity>()
    private lateinit var countryWithLeagueWithEventsAndTeamsItems: CountryWithLeagueWithEventsAndTeamsEntity
    private lateinit var eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsItems: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity


    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }


    override suspend fun insertCards(cards: List<CardEntity>) {
        cardItems.addAll(cards)
    }

    override suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>) {
        goalscorerItems.addAll(goalscorers)
    }

    override suspend fun insertLineups(lineups: List<LineupEntity>) {
        lineupItems.addAll(lineups)
    }

    override suspend fun insertStatistics(statistics: List<StatisticEntity>) {
        statisticsItems.addAll(statistics)
    }

    override suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>) {
        substitutionsItems.addAll(substitutions)
    }

    override suspend fun insertEvents(events: List<EventEntity>) {
        eventItems.addAll(events)
    }

    override suspend fun insertEventInformation(eventInformation: List<EventInformationEntity>) {
        eventInformationItems.addAll(eventInformation)
    }

    override suspend fun getEventsFromSpecificLeaguesFromDb(
        leagueId: String,
        date: String
    ): List<EventEntity> {
        return eventItems
    }

    override suspend fun refreshEventsFromSpecificLeagues(
        leagueId: String,
        date: String
    ): Resource<List<Event>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(eventItems.map { it.asDomainModel() })
        }
    }

    override suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeamsEntity {
        countryWithLeagueWithEventsAndTeamsItems =
            produceCountryWithLeagueWithEventsAndTeamsEntity()
        return countryWithLeagueWithEventsAndTeamsItems
    }

    override suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsItems
    }

    private fun produceCountryWithLeagueWithEventsAndTeamsEntity(): CountryWithLeagueWithEventsAndTeamsEntity {
        return CountryWithLeagueWithEventsAndTeamsEntity(
            produceCountryEntity(1),
            listOf(
                LeagueWithTeamsEntity(
                    produceLeagueEntity(1, 1),
                    listOf(
                        produceTeamEntity(1, 1),
                        produceTeamEntity(2, 1)
                    )
                )
            ),
            listOf(
                LeagueWithEventsEntity(
                    produceLeagueEntity(1, 1),
                    listOf(
                        EventWithEventInformationEntity(
                            eventItems[0],
                            eventInformationItems
                        )
                    )
                )
            )
        )
    }

    private fun produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
            produceEventEntity(1, 1),
            cardItems,
            goalscorerItems,
            lineupItems,
            statisticsItems,
            substitutionsItems
        )
    }
}
