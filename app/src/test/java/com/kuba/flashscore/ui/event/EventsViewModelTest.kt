package com.kuba.flashscore.ui.event

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.country.FakeCountryRepository
import com.kuba.flashscore.repositories.event.FakeEventRepository
import com.kuba.flashscore.repositories.league.FakeLeagueRepository
import com.kuba.flashscore.repositories.player.FakePlayerRepository
import com.kuba.flashscore.repositories.team.FakeTeamRepository
import com.kuba.flashscore.ui.events.EventsViewModel
import com.kuba.flashscore.ui.util.networking.FakeConnectivityManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class EventsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: EventsViewModel
    lateinit var eventRepository: FakeEventRepository
    lateinit var teamRepository: FakeTeamRepository
    lateinit var playerRepository: FakePlayerRepository
    lateinit var connectivityManager: FakeConnectivityManager

    @Before
    fun setup() {
        eventRepository = FakeEventRepository()
        teamRepository = FakeTeamRepository()
        playerRepository = FakePlayerRepository()
        connectivityManager = FakeConnectivityManager()

        val event1 =
            EventEntity(
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

        val event2 =
            EventEntity(
                "leagueId1",
                "matchDate2",
                "matchId2",
                "matchLive2",
                "matchReferee2",
                "matchRound2",
                "matchStadium2",
                "matchStatus2",
                "matchTime2"
            )


        val event3 =
            EventEntity(
                "leagueId3",
                "matchDate3",
                "matchId3",
                "matchLive3",
                "matchReferee3",
                "matchRound3",
                "matchStadium3",
                "matchStatus3",
                "matchTime3"
            )


        runBlockingTest {
            eventRepository.insertCards(listOf(produceCardEntity()))
            eventRepository.insertEventInformation(listOf(produceEventInformationAwayEntity()))
            eventRepository.insertGoalscorers(listOf(produceGoalscorerEntity()))
            eventRepository.insertLineups(listOf(produceLineupEntity()))
            eventRepository.insertStatistics(listOf(produceStatisticEntity()))
            eventRepository.insertSubstitutions(listOf(produceSubstitutionEntity()))
            eventRepository.insertEvents(listOf(event1, event2, event3))
        }
        viewModel =
            EventsViewModel(eventRepository, teamRepository, playerRepository, connectivityManager)
    }


    @Test
    fun `get country with league and events with teams from database, return success`() {
        viewModel.getCountryWithLeagueWithEventsAndTeams("countryId1", "matchDate1")

        val value = viewModel.countryWithLeagueWithTeamsAndEvents.getOrAwaitValueTest()

        assertThat(value.leagueWithEvents).isNotEmpty()
        assertThat(value.leagueWithEvents[0].eventsWithEventInformation).hasSize(1)
    }

    @Test
    fun `get event with all details and information from database, return success`() {
        viewModel.getPlayersAndCoachFromHomeTeam("teamId1")
        viewModel.getPlayersAndCoachFromAwayTeam("teamId2")
        viewModel.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions("countryId1")

        val value = viewModel.eventsWithDetailsWithHomeAndAwayTeams.getOrAwaitValueTest()

        assertThat(value.first.team.teamKey).isEqualTo("teamId1")
        assertThat(value.first.players).isNotEmpty()
        assertThat(value.first.coaches).isNotEmpty()

        assertThat(value.second.team.teamKey).isEqualTo("teamId1")
        assertThat(value.second.players).isNotEmpty()
        assertThat(value.second.coaches).isNotEmpty()

        assertThat(value.third.event.matchId).isEqualTo("matchId1")
        assertThat(value.third.cards).isNotEmpty()
        assertThat(value.third.cards).hasSize(1)
        assertThat(value.third.goalscorers).isNotEmpty()
        assertThat(value.third.goalscorers).hasSize(1)
        assertThat(value.third.lineups).isNotEmpty()
        assertThat(value.third.lineups).hasSize(1)
        assertThat(value.third.statistics).isNotEmpty()
        assertThat(value.third.statistics).hasSize(1)
        assertThat(value.third.substitutions).isNotEmpty()
        assertThat(value.third.substitutions).hasSize(1)
    }


    @Test
    fun `fetch correct data from network api, returns success`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        viewModel.refreshEvents("leagueId1", "matchDate1")
        delay(2000)
        val value = viewModel.countryWithLeagueWithTeamsAndEventsEntityStatus.getOrAwaitValueTest()

        assertThat(value.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(value.peekContent().data).isNotEmpty()
        assertThat(value.peekContent().data).hasSize(3)
    }

    @Test
    fun `fetch incorrect data from network api, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        eventRepository.setShouldReturnNetworkError(true)
        viewModel.refreshEvents("leagueId1", "matchDate1")
        delay(2000)
        val value = viewModel.countryWithLeagueWithTeamsAndEventsEntityStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

//    @Test
//    fun `network is not available, returns error`() = runBlocking {
//        connectivityManager.setNetworkAvailable(false)
//        eventRepository.setShouldReturnNetworkError(true)
//        viewModel.refreshEvents("leagueId1", "matchDate1")
//        delay(2000)
//        val value = viewModel.countryWithLeagueWithTeamsAndEventsEntityStatus.getOrAwaitValueTest()
//
//        assertThat(value.peekContent().status).isEqualTo(Status.ERROR)
//        assertThat(value.peekContent().message).isEqualTo(Constants.ERROR_MESSAGE)
//    }

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