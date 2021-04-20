package com.kuba.flashscore.ui.teams.standings.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.local.models.entities.StandingType
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.repositories.standings.FakeStandingsRepositoryAndroidTest
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.teams.standings.StandingsViewModel
import com.kuba.flashscore.util.DataProducerAndroid
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceStandingEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class StandingsHomeFragmentTest {

    private lateinit var leagueItem: LeagueEntity
    private lateinit var countryAndLeagueItem: CountryAndLeaguesEntity
    private lateinit var teamItems: List<TeamEntity>
    private lateinit var countryWithLeagueAndTeamsEntity: CountryWithLeagueAndTeamsEntity
    private lateinit var leagueWithTeamsItems: List<LeagueWithTeamsEntity>

    private lateinit var overallStandingsItems: List<StandingEntity>

    @Before
    fun setup() {

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

        overallStandingsItems = listOf(
            produceStandingEntity(1, 1, 1, StandingType.HOME),
            produceStandingEntity(2, 1, 2, StandingType.HOME)
        )
    }

    @Test
    fun clickStandingItem_navigateToClubViewPagerFragment() = runBlockingTest {
        val testStandingViewModel = StandingsViewModel(
            FakeStandingsRepositoryAndroidTest().also {
                it.insertStandings(overallStandingsItems)
            }, FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            }
        )

        //var testViewModel: StandingsViewModel? = null

        val scenario = launchFragmentInContainer<StandingsHomeFragment>()

        scenario.onFragment {
            it.setCountryWithLeagueAndTeams(countryWithLeagueAndTeamsEntity.asDomainModel())
            it.viewModel = testStandingViewModel
            it.standingsAdapter.standings = overallStandingsItems.map { it.asDomainModel() }
        }

//        launchFragmentInHiltContainer<StandingsHomeFragment>(
//            fragmentFactory = fragmentFactory
//        ) {
//            Navigation.setViewNavController(requireView(), navController)
//            viewModel = testStandingViewModel
//
//            //setCountryWithLeagueAndTeams(countryWithLeagueAndTeamsEntity.asDomainModel())
//
//            countryWithLeagueAndTeams2 = countryWithLeagueAndTeamsEntity.asDomainModel()
//            //await until this::isStandingsAdapterInitialized
//
//            standingsAdapter.standings = overallStandingsItems.map { it.asDomainModel() }
//        }

//        launchFragmentInHiltContainer<StandingsOverallFragment>(
//            fragmentFactory = fragmentFactory,
//            fragmentArgs = bundleOf("countryWithTeams" to countryWithLeagueAndTeamsEntity.asDomainModel())
//        ) {
//            navController.setGraph(R.navigation.nav_graph)
//            Navigation.setViewNavController(requireView(), navController)
//
//            countryWithLeagueAndTeams = countryWithLeagueAndTeamsEntity.asDomainModel()
//            viewModel = testStandingViewModel
//
//
//            //await until this::isStandingsAdapterInitialized
//
//            standingsAdapter.standings = overallStandingsItems.map { it.asDomainModel() }
//        }

        //await until { this@AwaitTest::result.isInitialized }
        onView(withId(R.id.recyclerViewOverallStandings))
            .perform(
                actionOnItemAtPosition<StandingsAdapter.StandingsViewHolder>(
                    0,
                    click()
                )
            )

//        verify(navController).navigate(
//            TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
//                overallStandingsItems[0].teamId,
//                countryWithLeagueAndTeamsEntity.asDomainModel()
//            )
//        )

        //testViewModel.getCountryWithLeagues("countryId1")

//        assertThat(testViewModel.countriesWithLeagues.getOrAwaitValue()).isEqualTo(
//            countryAndLeagueItems.asDomainModel()

    }

    suspend fun awaitInitialization() {
        while (!this::countryWithLeagueAndTeamsEntity.isInitialized) {
            delay(3000)
        }
    }

}
