package com.kuba.flashscore.ui.event.details

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.repositories.*
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.MainActivity
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.events.EventsViewModel
import com.kuba.flashscore.ui.events.details.EventDetailsViewPagerFragment
import com.kuba.flashscore.ui.events.details.EventDetailsViewPagerFragmentArgs
import com.kuba.flashscore.util.DataProducer.produceCardEntity
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceEventEntity
import com.kuba.flashscore.util.DataProducer.produceEventInformationEntity
import com.kuba.flashscore.util.DataProducer.produceGoalscorerEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.DataProducer.produceLineupEntity
import com.kuba.flashscore.util.DataProducer.produceStatisticEntity
import com.kuba.flashscore.util.DataProducer.produceSubstitutionEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class EventDetailsViewPagerFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory

    //private lateinit var countryAndLeagueItems: CountryAndLeaguesEntity
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

    private lateinit var eventsWithAllStuff: EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity


    @Before
    fun setup() {
        hiltRule.inject()
        leagueItem = produceLeagueEntity(1, 1)
//        countryAndLeagueItems = CountryAndLeaguesEntity(
//            produceCountryEntity(1),
//            listOf(leagueItem)
//        )

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
    }

    @Test
    fun informationAboutCountryAndLeagueAreCorrectlyDisplayed() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val testViewModel = EventsViewModel(FakeEventRepositoryAndroidTest().also {
            it.insertEvents(listOf(eventItem))
            it.insertEventInformation(eventInformationItems)
            it.insertCards(cardsItems)
            it.insertGoalscorers(goalscorersItems)
            it.insertLineups(lineupItems)
            it.insertStatistics(statisticsItems)
            it.insertSubstitutions(substitutionsItems)
        }, FakeTeamRepositoryAndroidTest().also {
            it.insertTeams(teamItems)
        },
            FakePlayerRepositoryAndroidTest(),
            FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            })

        val bundle = EventDetailsViewPagerFragmentArgs(
            eventItem.matchId,
            countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventDetailsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        onView(withId(R.id.textViewCountryName)).check(matches(withText("countryName1")))
        onView(withId(R.id.textViewLeagueName)).check(matches(withText("leagueName1 - matchRound1")))
    }


    @Test
    fun informationAboutTeamsAndScoreAreCorrectlyDisplayed() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val testViewModel = EventsViewModel(FakeEventRepositoryAndroidTest().also {
            it.insertEvents(listOf(eventItem))
            it.insertEventInformation(eventInformationItems)
            it.insertCards(cardsItems)
            it.insertGoalscorers(goalscorersItems)
            it.insertLineups(lineupItems)
            it.insertStatistics(statisticsItems)
            it.insertSubstitutions(substitutionsItems)
        }, FakeTeamRepositoryAndroidTest().also {
            it.insertTeams(teamItems)
        },
            FakePlayerRepositoryAndroidTest(),
            FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            })

        val bundle = EventDetailsViewPagerFragmentArgs(
            eventItem.matchId,
            countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventDetailsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        onView(withId(R.id.textViewFirstClubName)).check(matches(withText("teamName1")))
        onView(withId(R.id.textViewSecondClubName)).check(matches(withText("teamName2")))
        onView(withId(R.id.textViewDateAndTime)).check(matches(withText("matchDate1  matchTime1")))
        onView(withId(R.id.textViewFirstScore)).check(matches(withText("teamScore1")))
        onView(withId(R.id.textViewSecondScore)).check(matches(withText("teamScore2")))

    }


    @Test
    fun clickBackButton_popBackStack() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = EventDetailsViewPagerFragmentArgs(
            eventItem.matchId,
            countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventDetailsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            themeResId = R.style.MyActionBar,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withContentDescription("Navigate up")).perform(click());
        //onView(withId(android.R.id.home)).perform(click())
        //verify(navController).popBackStack()
    }

    private fun launchActivity(): ActivityScenario<MainActivity>? {
        return launch(MainActivity::class.java)
    }
}


