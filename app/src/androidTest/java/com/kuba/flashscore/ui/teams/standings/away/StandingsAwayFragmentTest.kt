package com.kuba.flashscore.ui.teams.standings.away

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.StandingsAdapter
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.local.models.entities.StandingType
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.repositories.standings.FakeStandingsRepositoryAndroidTest
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.teams.TeamsViewPagerFragmentDirections
import com.kuba.flashscore.ui.teams.standings.StandingsViewModel
import com.kuba.flashscore.util.DataProducerAndroid
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceStandingEntity
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
class StandingsAwayFragmentTest {

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

    private lateinit var awayStandingsItems: List<StandingEntity>

    lateinit var instrumentationContext: Context

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

        awayStandingsItems = listOf(
            produceStandingEntity(1, 1, 1, StandingType.AWAY),
            produceStandingEntity(2, 1, 2, StandingType.AWAY),
            produceStandingEntity(3, 1, 3, StandingType.AWAY),
            produceStandingEntity(4, 1, 4, StandingType.AWAY)
        )
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun clickStandingItem_navigateToClubViewPagerFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)

        val testStandingViewModel = StandingsViewModel(
            FakeStandingsRepositoryAndroidTest().also {
                it.insertStandings(awayStandingsItems)
            }, FakeConnectivityManager().also {
                it.isNetworkAvailable.postValue(true)
            }
        )

        launchFragmentInHiltContainer<StandingsAwayFragment>(
            fragmentFactory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)

            this.setCountryWithLeagueAndTeams(countryWithLeagueAndTeamsEntity.asDomainModel())
            this.standingsViewModel = testStandingViewModel
            this.standingsAdapter.standings = awayStandingsItems.map { it.asDomainModel() }

        }

        onView(withId(R.id.recyclerViewOverallStandings))
            .perform(
                actionOnItemAtPosition<StandingsAdapter.StandingsViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            TeamsViewPagerFragmentDirections.actionTeamsViewPagerFragmentToClubViewPagerFragment(
                awayStandingsItems[0].teamId,
                countryWithLeagueAndTeamsEntity.asDomainModel()
            )
        )
    }
}
