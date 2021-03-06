package com.kuba.flashscore.repositories.event

import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.event.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.mappers.event.EventDtoMapper
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class DefaultEventRepository @Inject constructor(
    private val cardDao: CardDao,
    private val goalscorerDao: GoalscorerDao,
    private val lineupDao: LineupDao,
    private val statisticDao: StatisticDao,
    private val substitutionDao: SubstitutionDao,
    private val eventsDao: EventDao,
    private val eventDtoMapper: EventDtoMapper,
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

    override suspend fun getEventsFromSpecificLeaguesFromDb(
        leagueId: String,
        date: String
    ): List<EventEntity> {
        return withContext(Dispatchers.IO) { eventsDao.getEventsFromSpecificLeague(leagueId, date) }
    }

    override suspend fun getEventsFromSpecificLeaguesFromNetwork(
        leagueId: String,
        date: String
    ): Resource<List<EventEntity>> {
        return try {
            val response = apiFootballService.getEvents(date, date, leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    eventsDao.insertEvents(
                        eventDtoMapper.toLocalList(
                            it?.toList()!!,
                            null
                        )
                    )
                    it.toList().forEach { eventDtoItem ->
                        if (eventDtoItem.cards.isNotEmpty()) {
                            insertCards(
                                eventDtoMapper.mapCardToLocal(
                                    eventDtoItem.cards,
                                    eventDtoItem.matchId
                                )
                            )
                        }
                        if (eventDtoItem.goalscorer.isNotEmpty()) {
                            insertGoalscorers(
                                eventDtoMapper.mapGoalscorerToLocal(
                                    eventDtoItem.goalscorer,
                                    eventDtoItem.matchId
                                )
                            )
                        }
                        if (eventDtoItem.statistics.isNotEmpty()) {
                            insertStatistics(
                                eventDtoMapper.mapStatisticToLocal(
                                    eventDtoItem.statistics,
                                    eventDtoItem.matchId
                                )
                            )
                        }
                        if (eventDtoItem.substitutions.away.isNotEmpty() || eventDtoItem.substitutions.home.isNotEmpty()) {
                            insertSubstitutions(
                                eventDtoMapper.mapSubstitutionsToLocal(
                                    eventDtoItem.substitutions,
                                    eventDtoItem.matchId
                                )
                            )
                        }
                        insertLineups(
                            eventDtoMapper.mapLineupToLocal(
                                eventDtoItem.lineup,
                                eventDtoItem.matchId
                            )
                        )
                    }
                    return Resource.success(eventsDao.getEventsFromSpecificLeague(leagueId, date))
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
    ): CountryWithLeagueWithEventsAndTeams {
        return eventsDao.getCountryWithLeagueWithTeamsAndEvents(leagueId, date)
    }

    override suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions {
        return eventsDao.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId)
    }


}