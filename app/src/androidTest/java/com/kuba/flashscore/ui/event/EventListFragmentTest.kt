package com.kuba.flashscore.ui.event

import android.widget.DatePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventAdapter
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.EventEntity
import com.kuba.flashscore.data.local.models.entities.event.EventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.*
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.events.EventsListFragment
import com.kuba.flashscore.ui.events.EventsListFragmentArgs
import com.kuba.flashscore.ui.events.EventsListFragmentDirections
import com.kuba.flashscore.ui.events.EventsViewModel
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceEventEntity
import com.kuba.flashscore.util.DataProducer.produceEventInformationEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity
import com.kuba.flashscore.util.MatcherUtils.withToolbarSubTitle
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
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
//@UninstallModules(AppModule::class)
@HiltAndroidTest
class EventListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory

    private lateinit var countryAndLeagueItems: CountryAndLeaguesEntity
    private lateinit var leagueItem: LeagueEntity
    private lateinit var teamItems: List<TeamEntity>

    private lateinit var eventWithEventInformation: EventWithEventInformationEntity
    private lateinit var eventItem: EventEntity
    private lateinit var eventInformationItems: List<EventInformationEntity>

    private lateinit var countryWithLeagueWithEventsAndTeamsEntity: CountryWithLeagueWithEventsAndTeamsEntity
    private lateinit var leagueWithTeamItems: List<LeagueWithTeamsEntity>
    private lateinit var leagueWithEventItems: List<LeagueWithEventsEntity>


    @Before
    fun setup() {
        hiltRule.inject()
        leagueItem = produceLeagueEntity(1, 1)
        countryAndLeagueItems = CountryAndLeaguesEntity(
            produceCountryEntity(1),
            listOf(leagueItem)
        )

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
    }

    @Test
    fun clickEventItemFromDatabase_navigateToEventDetailFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val testViewModel = EventsViewModel(FakeEventRepositoryAndroidTest().also {
            it.insertEvents(listOf(eventItem))
            it.insertEventInformation(eventInformationItems)
        }, FakeTeamRepositoryAndroidTest().also {
            it.insertTeams(teamItems)
        },
            FakePlayerRepositoryAndroidTest(),
            FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            })

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel

            eventsAdapter.events = listOf(eventWithEventInformation.asDomainModel())
            eventsAdapter.countryWithLeagueWithEventsAndTeams =
                countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        }

        onView(withId(R.id.recyclerViewEvents))
            .perform(
                actionOnItemAtPosition<EventAdapter.EventViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            EventsListFragmentDirections.actionEventsListFragmentToEventDetailsViewPagerFragment(
                eventItem.matchId,
                countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
            )
        )

        testViewModel.getCountryWithLeagueWithEventsAndTeams("leagueId1", "matchDate1")

        assertThat(testViewModel.countryWithLeagueWithTeamsAndEvents.getOrAwaitValue()).isEqualTo(
            countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        )
    }

    @Test
    fun clickEventItemFromNetwork_navigateToEventDetailFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val testViewModel = EventsViewModel(FakeEventRepositoryAndroidTest().also {
            it.insertEvents(listOf(eventItem))
            it.insertEventInformation(eventInformationItems)
        }, FakeTeamRepositoryAndroidTest().also {
            it.insertTeams(teamItems)
        },
            FakePlayerRepositoryAndroidTest(),
            FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            })

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel

            eventsAdapter.events = listOf(eventWithEventInformation.asDomainModel())
            eventsAdapter.countryWithLeagueWithEventsAndTeams =
                countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
        }

        onView(withId(R.id.recyclerViewEvents))
            .perform(
                actionOnItemAtPosition<EventAdapter.EventViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            EventsListFragmentDirections.actionEventsListFragmentToEventDetailsViewPagerFragment(
                eventItem.matchId,
                countryWithLeagueWithEventsAndTeamsEntity.asDomainModel()
            )
        )

        testViewModel.refreshEvents("leagueId1", "matchDate1")

        assertThat(
            testViewModel.countryWithLeagueWithTeamsAndEventsEntityStatus.getOrAwaitValue()
                .getContentIfNotHandled()?.status
        ).isEqualTo(
            Status.SUCCESS
        )
    }

    @Test
    fun clickImageButtonGoToLeagueTable_navigateToTeamsViewPagerFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.imageButtonGoToLeagueTable))
            .perform(click())


        verify(navController).navigate(
            EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                countryAndLeagueItems.asDomainModel()
            )
        )
    }


    @Test
    fun clickConstraintLayoutEventListTable_navigateToTeamsViewPagerFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.constraintLayoutEventListTable))
            .perform(click())


        verify(navController).navigate(
            EventsListFragmentDirections.actionEventsListFragmentToTeamsViewPagerFragment(
                countryAndLeagueItems.asDomainModel()
            )
        )
    }

    @Test
    fun clickBackButton_popBackStack() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            themeResId = R.style.MyActionBar,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withContentDescription("Navigate up")).perform(click());

        verify(navController).popBackStack()
    }

    @Test
    fun clickPickDateMenuItem_switchDate() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = EventsListFragmentArgs(
            countryAndLeagueItems.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<EventsListFragment>(
            fragmentFactory = fragmentFactory,
            themeResId = R.style.MyActionBar,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.actionMenuPickDate)).perform(click())
        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2020, 10, 30))
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.toolbarEventList)).check(matches(allOf(withToolbarSubTitle("30.10.2020, Friday"))))
    }
}

