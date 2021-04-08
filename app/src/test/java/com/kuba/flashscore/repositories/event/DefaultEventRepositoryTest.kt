package com.kuba.flashscore.repositories.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.daos.event.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.models.*
import com.kuba.flashscore.data.network.models.events.*
import com.kuba.flashscore.data.network.responses.CountryResponse
import com.kuba.flashscore.data.network.responses.EventResponse
import com.kuba.flashscore.data.network.responses.StandingResponse
import com.kuba.flashscore.data.network.responses.TeamResponse
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mockito.mock
import org.mockito.verification.VerificationMode
import retrofit2.Response
import timber.log.Timber
import javax.annotation.Resource

@ExperimentalCoroutinesApi
class DefaultEventRepositoryTest {

    //Test subject
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var eventRepository: EventRepository

    //Collaborators
    private lateinit var cardDao: CardDao
    private lateinit var goalscorerDao: GoalscorerDao
    private lateinit var lineupDao: LineupDao
    private lateinit var statisticDao: StatisticDao
    private lateinit var substitutionDao: SubstitutionDao
    private lateinit var eventsDao: EventDao
    private lateinit var footballApiService: ApiFootballService

    //Utilities
    lateinit var eventsFromApi: Response<EventResponse>
    lateinit var eventFromDao: EventEntity
    lateinit var eventInformationHomeTeamFromDao: EventInformationEntity
    lateinit var eventInformationAwayTeamFromDao: EventInformationEntity
    lateinit var cardsFromDao: CardEntity
    lateinit var goalscorerFromDao: GoalscorerEntity
    lateinit var statisticFromDao: StatisticEntity
    lateinit var substitutionFromDao: SubstitutionsEntity
    lateinit var lineupFromDao: LineupEntity
    lateinit var countryWithLeagueWithEventsAndTeamsFromDao: CountryWithLeagueWithEventsAndTeamsEntity
    lateinit var eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsFromDao: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity

    @Before
    fun setup() = runBlockingTest {

        //mocking dao
        cardDao = mock()
        goalscorerDao = mock()
        lineupDao = mock()
        statisticDao = mock()
        substitutionDao = mock()
        eventsDao = mock()

        eventFromDao = produceEventEntity()
        eventInformationHomeTeamFromDao = produceEventInformationHomeEntity()
        eventInformationAwayTeamFromDao = produceEventInformationAwayEntity()
        cardsFromDao = produceCardEntity()
        goalscorerFromDao = produceGoalscorerEntity()
        statisticFromDao = produceStatisticEntity()
        substitutionFromDao = produceSubstitutionEntity()
        lineupFromDao = produceLineupEntity()
        countryWithLeagueWithEventsAndTeamsFromDao =
            produceCountryWithLeagueWithEventsAndTeamsEntity()
        eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsFromDao =
            produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity()

        whenever(
            eventsDao.getCountryWithLeagueWithTeamsAndEvents(any(), any())
        ).thenReturn(
            countryWithLeagueWithEventsAndTeamsFromDao
        )

        whenever(
            eventsDao.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
                any()
            )
        ).thenReturn(
            eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsFromDao
        )

        whenever(
            eventsDao.getEventsFromSpecificLeague(
                any(),
                any()
            )
        ).thenReturn(listOf(eventFromDao))


        //mocking api
        footballApiService = mock()
        eventsFromApi = produceEventResponseSuccess()
        whenever(footballApiService.getEvents("2021-03-24", "2021-03-24", "leagueId1")).thenReturn(
            eventsFromApi
        )

        eventRepository = DefaultEventRepository(
            cardDao,
            goalscorerDao,
            lineupDao,
            statisticDao,
            substitutionDao,
            eventsDao,
            footballApiService
        )
    }

    @Test
    fun getEventsFromSpecificLeagueFromDatabase_shouldReturnEvents() = runBlockingTest {
        eventRepository.getEventsFromSpecificLeaguesFromDb("leagueId1", "2021-03-24")

        verify(eventsDao, times(1)).getEventsFromSpecificLeague("leagueId1", "2021-03-24")
    }

    @Test
    fun getCountryWithLeagueWithTeamsAndEventsFromDatabase_shouldReturnEvents() = runBlockingTest {
        eventRepository.getCountryWithLeagueWithTeamsAndEvents("leagueId1", "2021-03-24")

        verify(eventsDao, times(1)).getCountryWithLeagueWithTeamsAndEvents(
            "leagueId1",
            "2021-03-24"
        )
    }

    @Test
    fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutionsFromDatabase_shouldReturnEvents() =
        runBlockingTest {
            eventRepository.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions("eventId1")
            verify(
                eventsDao,
                times(1)
            ).getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
                "eventId1"
            )
        }


    @Test
    fun getEventsFromDatabaseAndEventsExists_shouldNotCallToApiService() = runBlockingTest {
        eventRepository.getEventsFromSpecificLeaguesFromDb("leagueId1", "2021-03-24")
        verify(footballApiService, never()).getEvents("2021-03-24", "2021-03-24", "leagueId1")
    }


    @Test
    fun ifDaoReturnsAllEventsThenRepositoryReturnsSameEvents() = runBlockingTest {
        val events =
            eventRepository.getEventsFromSpecificLeaguesFromDb("leagueId1", "2021-03-24")
        assertThat(events).hasSize(1)
        assertThat(events[0]).isEqualTo(eventFromDao)
    }

    @Test
    fun ifDaoReturnsCountryWithLeagueWithTeamsAndEventsThenRepositoryReturnsSameItems() =
        runBlockingTest {
            val countryWithLeagueWithTeamsAndEvents =
                eventRepository.getCountryWithLeagueWithTeamsAndEvents("leagueId1", "2021-03-24")
            assertThat(countryWithLeagueWithTeamsAndEvents).isEqualTo(
                countryWithLeagueWithEventsAndTeamsFromDao
            )
        }

    @Test
    fun ifDaoReturnsEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutionsThenRepositoryReturnsSameEvents() =
        runBlockingTest {
            val eventsWithDetails =
                eventRepository.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
                    "eventId1"
                )
            assertThat(eventsWithDetails).isEqualTo(
                eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsFromDao
            )
        }

    @Test
    fun getEventsFromDatabaseIsEmpty_shouldCallToApiService() = runBlockingTest {
        whenever(eventsDao.getEventsFromSpecificLeague("leagueId1", "2021-03-24")).thenReturn(null)

        eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

        verify(footballApiService, times(1)).getEvents("2021-03-24", "2021-03-24", "leagueId1")
        verify(eventsDao, times(1)).getEventsFromSpecificLeague("leagueId1", "2021-03-24")
        verify(eventsDao, times(1)).insertEvents(any())
        verify(eventsDao, times(2)).insertEventInformation(any())
        verify(cardDao, times(1)).insertCards(any())
        verify(goalscorerDao, times(1)).insertGoalscorers(any())
        verify(statisticDao, times(1)).insertStatistics(any())
        verify(substitutionDao, times(0)).insertSubstitutions(any())
        verify(lineupDao, times(0)).insertLineup(any())
    }


    @Test
    fun apiServiceIsCallBeforeCallEventDaoIfDatabaseIsEmpty() = runBlockingTest {
        whenever(eventsDao.getEventsFromSpecificLeague("leagueId1", "2021-03-24")).thenReturn(null)

        eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

        val orderVerifier: InOrder = inOrder(footballApiService, eventsDao)
        orderVerifier.verify(footballApiService).getEvents("2021-03-24", "2021-03-24", "leagueId1")
        orderVerifier.verify(eventsDao)
            .getEventsFromSpecificLeague("leagueId1", "2021-03-24")
    }

    @Test
    fun apiServiceIsCallBeforeCallTeamDaoIfDatabaseIsEmptyAndDataAreInsertInCorrectWay() =

        runBlockingTest {
            whenever(eventsDao.getEventsFromSpecificLeague("leagueId1", "2021-03-24")).thenReturn(
                null
            )

            eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

            val orderVerifier: InOrder = inOrder(
                footballApiService,
                eventsDao,
                cardDao,
                goalscorerDao,
                lineupDao,
                statisticDao,
                substitutionDao
            )

            orderVerifier.verify(footballApiService)
                .getEvents("2021-03-24", "2021-03-24", "leagueId1")
            orderVerifier.verify(eventsDao).insertEvents(any())
            orderVerifier.verify(cardDao).insertCards(any())
            orderVerifier.verify(goalscorerDao).insertGoalscorers(any())
            orderVerifier.verify(statisticDao).insertStatistics(any())
            orderVerifier.verify(eventsDao)
                .getEventsFromSpecificLeague("leagueId1", "2021-03-24")
        }

    @Test
    fun refreshEventsFromNetwork_eventsShouldAlsoInsertToDatabase() = runBlockingTest {
        whenever(eventsDao.getEventsFromSpecificLeague("leagueId1", "2021-03-24")).thenReturn(null)

        eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

        verify(eventsDao, times(1)).insertEvents(any())
        verify(eventsDao, times(2)).insertEventInformation(any())
        verify(cardDao, times(1)).insertCards(any())
        verify(goalscorerDao, times(1)).insertGoalscorers(any())
        verify(statisticDao, times(1)).insertStatistics(any())
        verify(substitutionDao, times(0)).insertSubstitutions(any())
        verify(lineupDao, times(0)).insertLineup(any())

        whenever(eventsDao.getEventsFromSpecificLeague("leagueId1", "2021-03-24")).thenReturn(
            listOf(eventFromDao)
        )

        val events =
            eventRepository.getEventsFromSpecificLeaguesFromDb("leagueId1", "2021-03-24")
        assertThat(events).hasSize(1)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeError_returnError() = runBlockingTest {
        whenever(footballApiService.getEvents("2021-03-24", "2021-03-24", "leagueId1")).thenReturn(
            produceTeamResponseError()
        )

        val events = eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

        verify(eventsDao, never()).getEventsFromSpecificLeague("leagueId1", "2021-03-24")

        verify(footballApiService, times(1)).getEvents("2021-03-24", "2021-03-24", "leagueId1")
        assertThat(events.status).isEqualTo(Status.ERROR)
        assertThat(events.data).isEqualTo(null)
        assertThat(events.message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeErrorAndDatabaseIsAlsoEmpty_returnError() =
        runBlockingTest {
            whenever(
                footballApiService.getEvents(
                    "2021-03-24",
                    "2021-03-24",
                    "leagueId1"
                )
            ).thenReturn(
                produceTeamResponseError()
            )
            whenever(
                eventRepository.getEventsFromSpecificLeaguesFromDb(
                    "leagueId1",
                    "2021-03-24"
                )
            ).thenReturn(null)
            eventRepository.refreshEventsFromSpecificLeagues("leagueId1", "2021-03-24")

            val eventsFromDatabase =
                eventRepository.getEventsFromSpecificLeaguesFromDb("leagueId1", "2021-03-24")

            verify(eventsDao, times(1)).getEventsFromSpecificLeague("leagueId1", "2021-03-24")
            verify(footballApiService, times(1)).getEvents("2021-03-24", "2021-03-24","leagueId1")
            assertThat(eventsFromDatabase).isNull()
        }

    private fun produceEventResponseSuccess(): Response<EventResponse> {
        val eventResponse = EventResponse()
        eventResponse.add(
            0,
            EventDto(
                produceCardDtos(),
                "countryId1",
                "countryLogo1",
                "countryName1",
                produceGoalscorerDtos(),
                "leagueId1",
                "leagueLogo1",
                "leagueName1",
                Lineup(produceAwayLineupDtos(), produceHomeLineupDtos()),
                "matchAwayTeamExtraScore2",
                "matchAwayTeamFtScore2",
                "matchAwayTeamHalfTimeScore2",
                "matchAwayTeamId2",
                "matchAwayTeamName2",
                "matchAwayTeamPenaltyScore2",
                "matchAwayTeamScore2",
                "matchAwayTeamSystem2",
                "matchDate2",
                "matchHomeTeamExtraScore2",
                "matchHomeTeamFtScore2",
                "matchHomeTeamHalfTimeScore2",
                "matchHomeTeamId2",
                "matchHomeTeamName2",
                "matchHomeTeamPenaltyScore2",
                "matchHomeTeamScore2",
                "matchHomeTeamSystem2",
                "matchId2",
                "matchLive2",
                "matchReferee2",
                "matchRound2",
                "matchStadium2",
                "matchStatus2",
                "matchTime2",
                produceStatisticsDtos(),
                produceSubstitutionsDtos(),
                "teamAwayBadge2",
                "teamHomeBadge2"
            )
        )
        return Response.success(eventResponse)
    }

    private fun produceTeamResponseError(): Response<EventResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }

    private fun produceCardDtos(): List<Card> {
        return listOf(
            Card(
                "awayFault2",
                "card2",
                "homeFault2",
                "info2",
                "time2"
            )
        )
    }

    private fun produceGoalscorerDtos(): List<Goalscorer> {
        return listOf(
            Goalscorer(
                "awayAssist2",
                "awayAssistId2",
                "awayScorer2",
                "awayScorerId2",
                "homeAssist2",
                "homeAssistId2",
                "homeScorer2",
                "homeScorerId2",
                "info2",
                "score2",
                "time2"
            )
        )
    }

    private fun produceAwayLineupDtos(): Away {
        return Away(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )

    }

    private fun produceHomeLineupDtos(): Home {
        return Home(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )

    }

    private fun produceStatisticsDtos(): List<Statistic> {
        return listOf(
            Statistic(
                "away2",
                "home2",
                "type2"
            )
        )
    }

    private fun produceSubstitutionsDtos(): Substitutions {
        return Substitutions(
            emptyList(),
            emptyList()
        )

    }

    private fun produceEventEntity(): EventEntity {
        return EventEntity(
            "leagueId1",
            "matchDate1",
            "matchId1",
            "matchLive1",
            "matchReferee1",
            "matchRound1",
            "matchStadium1",
            "matchStatus1",
            "matchTime1"
        )
    }

    fun produceEventInformationHomeEntity(): EventInformationEntity {
        return EventInformationEntity(
            "matchId1",
            "extraScore1",
            "fullTimeScore1",
            "halfTimeScore1",
            "teamId1",
            "teamPenaltyScore1",
            "teamScore1",
            "teamSystem1",
            true
        )
    }


    fun produceEventInformationAwayEntity(): EventInformationEntity {
        return EventInformationEntity(
            "matchId1",
            "extraScore2",
            "fullTimeScore2",
            "halfTimeScore2",
            "teamId2",
            "teamPenaltyScore2",
            "teamScore2",
            "teamSystem2",
            false
        )
    }

    fun produceCardEntity(): CardEntity {
        return CardEntity(
            "matchId1",
            "fault1",
            "card1",
            true,
            "info1",
            "time1"
        )
    }

    fun produceGoalscorerEntity(): GoalscorerEntity {
        return GoalscorerEntity(
            "matchId1",
            "assistId1",
            "scorerId1",
            true,
            "info1",
            "score1",
            "time1"
        )
    }

    fun produceStatisticEntity(): StatisticEntity {
        return StatisticEntity(
            "matchId1",
            "away1",
            "home1",
            "type1"
        )
    }

    fun produceSubstitutionEntity(): SubstitutionsEntity {
        return SubstitutionsEntity(
            "matchId1",
            "substitutions1",
            "time1",
            true
        )
    }

    fun produceLineupEntity(): LineupEntity {
        return LineupEntity(
            "matchId1",
            "lineupNumber1",
            "lineupPosition1",
            true,
            true,
            false,
            "playerKey1"
        )
    }

    fun produceCountryWithLeagueWithEventsAndTeamsEntity(): CountryWithLeagueWithEventsAndTeamsEntity {
        return CountryWithLeagueWithEventsAndTeamsEntity(
            CountryEntity("countryId1", "countryLogo1", "countryName1"),
            listOf(
                LeagueWithTeamsEntity(
                    LeagueEntity(
                        "countryId1",
                        "leagueId1",
                        "leagueLogo1",
                        "leagueName1",
                        "leagueSeason1"
                    ),
                    listOf(
                        TeamEntity(
                            "leagueId1",
                            "teamBadge1",
                            "teamId1",
                            "teamName1",

                            )
                    )
                )
            ),
            listOf(
                LeagueWithEventsEntity(
                    LeagueEntity(
                        "countryId1",
                        "leagueId1",
                        "leagueLogo1",
                        "leagueName1",
                        "leagueSeason1"
                    ),
                    listOf(
                        EventWithEventInformationEntity(
                            produceEventEntity(),
                            listOf(
                                produceEventInformationHomeEntity(),
                                produceEventInformationAwayEntity()
                            )
                        )
                    )
                )
            )
        )
    }

    fun produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
            produceEventEntity(),
            listOf(produceCardEntity()),
            listOf(produceGoalscorerEntity()),
            listOf(produceLineupEntity()),
            listOf(produceStatisticEntity()),
            listOf(produceSubstitutionEntity())
        )
    }
}