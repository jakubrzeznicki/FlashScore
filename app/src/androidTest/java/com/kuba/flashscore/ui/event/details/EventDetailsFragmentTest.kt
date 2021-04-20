package com.kuba.flashscore.ui.event.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kuba.flashscore.R
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.local.models.entities.CoachEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.PlayerEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.events.details.EventDetailsFragment
import com.kuba.flashscore.util.DataProducerAndroid.produceCardEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceCoachEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceEventEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceEventInformationEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceGoalscorerEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLineupEntity
import com.kuba.flashscore.util.DataProducerAndroid.producePlayerEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceStatisticEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceSubstitutionEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceTeamEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class EventDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory

    private lateinit var leagueItem: LeagueEntity
    private lateinit var teamItems: List<TeamEntity>

    private lateinit var eventWithEventInformation: EventWithEventInformationEntity
    private lateinit var eventItem: EventEntity
    private lateinit var eventInformationItems: List<EventInformationEntity>

    private lateinit var countryWithLeagueWithEventsAndTeamsEntity: CountryWithLeagueWithEventsAndTeamsEntity
    private lateinit var leagueWithTeamItems: List<LeagueWithTeamsEntity>
    private lateinit var leagueWithEventItems: List<LeagueWithEventsEntity>

    private lateinit var cardsItems: List<CardEntity>
    private lateinit var goalscorersItems: List<GoalscorerEntity>
    private lateinit var substitutionsItems: List<SubstitutionsEntity>
    private lateinit var lineupItems: List<LineupEntity>
    private lateinit var statisticsItems: List<StatisticEntity>

    private lateinit var playersItems: List<PlayerEntity>
    private lateinit var coachItems: List<CoachEntity>

    private lateinit var eventsWithAllStuff: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
    private lateinit var homeTeam: TeamWithPlayersAndCoach
    private lateinit var awayTeam: TeamWithPlayersAndCoach


    @Before
    fun setup() {
        hiltRule.inject()
        leagueItem = produceLeagueEntity(1, 1)

        teamItems = listOf(
            produceTeamEntity(1, 1),
            produceTeamEntity(2, 1)
        )
        eventItem = produceEventEntity(1, 1)
        eventInformationItems = listOf(
            produceEventInformationEntity(1, 1, 1, true),
            produceEventInformationEntity(2, 1, 2, false)
        )

        eventWithEventInformation = EventWithEventInformationEntity(
            eventItem,
            eventInformationItems
        )

        leagueWithEventItems = listOf(
            LeagueWithEventsEntity(
                leagueItem,
                listOf(eventWithEventInformation)
            )
        )

        leagueWithTeamItems = listOf(
            LeagueWithTeamsEntity(
                leagueItem,
                teamItems
            )
        )

        countryWithLeagueWithEventsAndTeamsEntity = CountryWithLeagueWithEventsAndTeamsEntity(
            produceCountryEntity(1),
            leagueWithTeamItems,
            leagueWithEventItems
        )

        cardsItems = listOf(
            produceCardEntity(1, 1, true),
            produceCardEntity(2, 1, false)
        )

        goalscorersItems = listOf(
            produceGoalscorerEntity(1, 1, true),
            produceGoalscorerEntity(2, 1, false)
        )

        lineupItems = listOf(
            produceLineupEntity(1, 1, true),
            produceLineupEntity(2, 1, false)
        )

        statisticsItems = listOf(
            produceStatisticEntity(1, 1),
            produceStatisticEntity(2, 1)
        )

        substitutionsItems = listOf(
            produceSubstitutionEntity(1, 1, true),
            produceSubstitutionEntity(2, 1, false)
        )

        eventsWithAllStuff =
            EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
                eventItem,
                cardsItems,
                goalscorersItems,
                lineupItems,
                statisticsItems,
                substitutionsItems
            )

        playersItems = listOf(
            producePlayerEntity(1, 1),
            producePlayerEntity(2, 1),
            producePlayerEntity(3, 1)
        )
        coachItems = listOf(
            produceCoachEntity(1, 1)
        )

        homeTeam = TeamWithPlayersAndCoachEntity(
            teamItems[0],
            playersItems,
            coachItems
        ).asDomainModel()

        awayTeam = TeamWithPlayersAndCoachEntity(
            teamItems[0],
            playersItems,
            coachItems
        ).asDomainModel()
    }

    @Test
    fun informationAboutFirstHalfIsCorrectlyDisplayed() = runBlockingTest {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<EventDetailsFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            setArgumentsVariables(
                eventWithEventInformation.asDomainModel(),
                eventsWithAllStuff.asDomainModel(),
                homeTeam,
                awayTeam
            )
        }

        onView(withId(R.id.textViewInformationAboutFirstHalfResult)).check(matches(withText("halfTimeScore1  - halfTimeScore2")))
    }
}


