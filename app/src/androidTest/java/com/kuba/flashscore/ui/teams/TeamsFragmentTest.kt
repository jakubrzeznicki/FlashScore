package com.kuba.flashscore.ui.teams

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.adapters.TeamsAdapter
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.FakeLeagueRepositoryAndroidTest
import com.kuba.flashscore.repositories.FakeTeamRepositoryAndroidTest
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.util.DataProducer
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.MatcherUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class TeamsFragmentTest {

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
            DataProducer.produceTeamEntity(1, 1),
            DataProducer.produceTeamEntity(2, 1)
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
    fun clickTeamItem_navigateToClubViewPagerFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val bundle = TeamsViewPagerFragmentArgs(
            countryAndLeagueItem.asDomainModel()
        ).toBundle()

        val testViewModel = TeamsViewModel(
            FakeTeamRepositoryAndroidTest().also {
                it.insertTeams(teamItems)
            }
        )
        launchFragmentInHiltContainer<TeamsFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            teamsAdapter.teams = teamItems.map { it.asDomainModel() }
            teamsAdapter.countryWithLeagueAndTeams = countryWithLeagueAndTeamsEntity.asDomainModel()
        }

        onView(withId(R.id.recyclerViewTeams))
            .perform(
                actionOnItemAtPosition<TeamsAdapter.TeamViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
                teamItems[0].teamKey,
                countryWithLeagueAndTeamsEntity.asDomainModel()
            )
        )

        //testViewModel.getCountryWithLeagues("countryId1")

//        assertThat(testViewModel.countriesWithLeagues.getOrAwaitValue()).isEqualTo(
//            countryAndLeagueItems.asDomainModel()

    }


}
