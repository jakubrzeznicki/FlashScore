package com.kuba.flashscore.ui.teams

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.repositories.team.FakeTeamRepositoryAndroidTest
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.util.DataProducerAndroid
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class TeamsViewPagerFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory

    private lateinit var leagueItem: LeagueEntity
    private lateinit var countryAndLeagueItem: CountryAndLeaguesEntity
    private lateinit var teamItems: List<TeamEntity>
    private lateinit var countryWithLeagueAndTeamsEntity: CountryWithLeagueAndTeamsEntity
    private lateinit var leagueWithTeamsItems: List<LeagueWithTeamsEntity>

    @Before
    fun setup() {
        hiltRule.inject()

        leagueItem = produceLeagueEntity(1, 1)
        countryAndLeagueItem = CountryAndLeaguesEntity(
            produceCountryEntity(1),
            listOf(leagueItem)
        )

        teamItems = listOf(
            DataProducerAndroid.produceTeamEntity(1, 1),
            DataProducerAndroid.produceTeamEntity(2, 1)
        )

        leagueWithTeamsItems = listOf(
            LeagueWithTeamsEntity(
                leagueItem,
                teamItems
            )
        )
        countryWithLeagueAndTeamsEntity = CountryWithLeagueAndTeamsEntity(
            produceCountryEntity(1),
            leagueWithTeamsItems
        )
    }

    @Test
    fun informationAboutCountryAndLeagueAreCorrectlyDisplayed() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = TeamsViewPagerFragmentArgs(
            countryAndLeagueItem.asDomainModel()
        ).toBundle()

        val testViewModel = TeamsViewModel(
            FakeTeamRepositoryAndroidTest().also {
                it.insertTeams(teamItems)
            }
        )
        launchFragmentInHiltContainer<TeamsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        onView(withId(R.id.textViewCountryName)).check(matches(withText("countryName1")))
        onView(withId(R.id.textViewLeagueName)).check(matches(withText("leagueName1")))

    }

    @Test
    fun clickBackButton_popBackStack() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = TeamsViewPagerFragmentArgs(
            countryAndLeagueItem.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<TeamsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withContentDescription("Navigate up")).perform(click())

        verify(navController).popBackStack()
    }

    @Test
    fun tabLayoutAndViewPagerIsCorrectlyDisplayed() {
        val navController = mock(NavController::class.java)

        val bundle = TeamsViewPagerFragmentArgs(
            countryAndLeagueItem.asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<TeamsViewPagerFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.viewPagerTeams)).check(matches(isDisplayed()))
        onView(withId(R.id.tabLayoutTeams2)).check(matches(isDisplayed()))
    }
}