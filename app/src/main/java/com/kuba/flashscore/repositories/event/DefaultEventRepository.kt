package com.kuba.flashscore.repositories.event

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.CoachDtoMapper
import com.kuba.flashscore.network.mappers.PlayerDtoMapper
import com.kuba.flashscore.network.mappers.TeamDtoMapper
import com.kuba.flashscore.network.mappers.event.EventDtoMapper
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
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
        from: String,
        to: String
    ): Resource<List<EventEntity>> {
        return try {
            val response = apiFootballService.getEvents(from, to, leagueId)
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
                    return Resource.success(eventsDao.getEventsFromSpecificLeague(leagueId, from))
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE, null)
        }
    }

    override suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        from: String,
        to: String
    ): CountryWithLeagueWithEventsAndTeams {
        return eventsDao.getCountryWithLeagueWithTeamsAndEvents(leagueId, from)
    }

    override suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions {

        return eventsDao.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId)
    }


}