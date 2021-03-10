package com.kuba.flashscore.repositories.event

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.daos.event.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultEventRepository @Inject constructor(
    private val cardDao: CardDao,
    private val goalscorerDao: GoalscorerDao,
    private val lineupDao: LineupDao,
    private val statisticDao: StatisticDao,
    private val substitutionDao: SubstitutionDao,
    private val eventsDao: EventDao,
    private val apiFootballService: ApiFootballService
) : EventRepository {
    override suspend fun insertCards(cards: List<CardEntity>) {
        cardDao.insertCards(cards)
    }

    override suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>) {
        goalscorerDao.insertGoalscorers(goalscorers)
    }

    override suspend fun insertLineups(lineups: List<LineupEntity>) {
        lineupDao.insertLineup(lineups)
    }

    override suspend fun insertStatistics(statistics: List<StatisticEntity>) {
        statisticDao.insertStatistics(statistics)
    }

    override suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>) {
        substitutionDao.insertSubstitutions(substitutions)
    }

    override suspend fun insertEvents(events: List<EventEntity>) {
        eventsDao.insertEvents(events)
    }

    override suspend fun insertEventInformation(eventInformation: List<EventInformationEntity>) {
        eventsDao.insertEventInformation(eventInformation)
    }

    override suspend fun getEventsFromSpecificLeaguesFromDb(
        leagueId: String,
        date: String
    ): List<EventEntity> {
        return eventsDao.getEventsFromSpecificLeague(leagueId, date)
    }

    override suspend fun refreshEventsFromSpecificLeagues(
        leagueId: String,
        date: String
    ): Resource<List<Event>> {
        return try {
            val response = apiFootballService.getEvents(date, date, leagueId)
            if (response.isSuccessful) {
                response.body().let { eventResponse ->
                    eventsDao.apply {
                        insertEvents(eventResponse?.toList()?.map { it.asLocalModel() }!!)
                        insertEventInformation(
                            eventResponse.toList().map { it.asLocalModelHomeTeamInformation() })
                        insertEventInformation(
                            eventResponse.toList().map { it.asLocalModelAwayTeamInformation() })
                    }
                    eventResponse?.toList()?.forEach { eventDtoItem ->
                        if (!eventDtoItem.cards.isNullOrEmpty()) {
                            insertCards(
                                eventDtoItem.cards.map { it.asLocalModel(eventDtoItem.matchId) }
                            )
                        }
                        if (!eventDtoItem.goalscorer.isNullOrEmpty()) {
                            insertGoalscorers(
                                eventDtoItem.goalscorer.map { it.asLocalModel(eventDtoItem.matchId) }
                            )
                        }
                        if (!eventDtoItem.statistics.isNullOrEmpty()) {
                            insertStatistics(
                                eventDtoItem.statistics.map { it.asLocalModel(eventDtoItem.matchId) }
                            )
                        }
                        if (!eventDtoItem.substitutions.home.isNullOrEmpty()) {
                            insertSubstitutions(
                                eventDtoItem.substitutions.home.map { it.asLocalModel(eventDtoItem.matchId) }
                            )
                        }
                        if (!eventDtoItem.substitutions.away.isNullOrEmpty()) {
                            insertSubstitutions(
                                eventDtoItem.substitutions.away.map { it.asLocalModel(eventDtoItem.matchId) }
                            )
                        }
                        if (!eventDtoItem.lineup.home.startingLineups.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.home.startingLineups.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                        if (!eventDtoItem.lineup.home.missingPlayers.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.home.missingPlayers.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                        if (!eventDtoItem.lineup.home.substitutes.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.home.substitutes.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                        if (!eventDtoItem.lineup.away.startingLineups.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.away.startingLineups.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                        if (!eventDtoItem.lineup.away.missingPlayers.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.away.missingPlayers.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                        if (!eventDtoItem.lineup.away.substitutes.isNullOrEmpty()) {
                            insertLineups(
                                eventDtoItem.lineup.away.substitutes.map {
                                    it.asLocalModel(
                                        eventDtoItem.matchId
                                    )
                                }
                            )
                        }
                    }
                    return Resource.success(
                        eventsDao.getEventsFromSpecificLeague(leagueId, date)
                            .map { it.asDomainModel() })
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeamsEntity {
        return eventsDao.getCountryWithLeagueWithTeamsAndEvents(leagueId, date)
    }

    override suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return eventsDao.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
            eventId
        )
    }


}