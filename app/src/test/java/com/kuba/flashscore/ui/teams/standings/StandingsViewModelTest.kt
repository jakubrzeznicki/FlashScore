package com.kuba.flashscore.ui.teams.standings

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.local.models.entities.StandingType
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.country.FakeCountryRepository
import com.kuba.flashscore.repositories.league.FakeLeagueRepository
import com.kuba.flashscore.repositories.standing.FakeStandingRepository
import com.kuba.flashscore.ui.util.networking.FakeConnectivityManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StandingsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StandingsViewModel
    lateinit var standingRepository: FakeStandingRepository
    lateinit var connectivityManager: FakeConnectivityManager

    @Before
    fun setup() {
        standingRepository = FakeStandingRepository()
        connectivityManager = FakeConnectivityManager()


        val standing1 = StandingEntity(
            "leagueRound1",
            "leagueD1",
            "leagueGA1",
            "leagueGF1",
            "leagueL1",
            "leaguePTS1",
            "leaguePayed1",
            "leaguePosition1",
            "leagueW1",
            "promotion1",
            "teamId1",
            "leagueId1",
            StandingType.OVERALL
        )

        val standing2 = StandingEntity(
            "leagueRound2",
            "leagueD2",
            "leagueGA2",
            "leagueGF2",
            "leagueL2",
            "leaguePTS2",
            "leaguePayed2",
            "leaguePosition2",
            "leagueW2",
            "promotion2",
            "teamId2",
            "leagueId1",
            StandingType.AWAY
        )

        val standing3 = StandingEntity(
            "leagueRound3",
            "leagueD3",
            "leagueGA3",
            "leagueGF3",
            "leagueL3",
            "leaguePTS3",
            "leaguePayed3",
            "leaguePosition3",
            "leagueW3",
            "promotion3",
            "teamId3",
            "leagueId1",
            StandingType.HOME
        )
        runBlockingTest {
            standingRepository.insertStandings(listOf(standing1, standing2, standing3))
        }
        viewModel = StandingsViewModel(standingRepository, connectivityManager)
    }


    @Test
    fun `insert overall standing item and get from database, return success`() {
        viewModel.getOverallStandingsFromSpecificLeague("leagueId1")

        val value = viewModel.standings.getOrAwaitValueTest()

        assertThat(value).isNotEmpty()
        assertThat(value).hasSize(1)
    }

    @Test
    fun `insert away standing item and get from database, return success`() {
        viewModel.getAwayStandingsFromSpecificLeague("leagueId1")

        val value = viewModel.standings.getOrAwaitValueTest()

        assertThat(value).isNotEmpty()
        assertThat(value).hasSize(1)
    }

    @Test
    fun `insert home standing item and get from database, return success`() {
        viewModel.getHomeStandingsFromSpecificLeague("leagueId1")

        val value = viewModel.standings.getOrAwaitValueTest()

        assertThat(value).isNotEmpty()
        assertThat(value).hasSize(1)
    }

    @Test
    fun `fetch correct data from network api, returns success`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        viewModel.refreshStandingsFromSpecificLeague("leagueId1")
        val value = viewModel.standingsStatus.getOrAwaitValueTest()

        assertThat(value.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(value.peekContent().data).isNotEmpty()
        assertThat(value.peekContent().data).hasSize(3)
    }

    @Test
    fun `fetch incorrect data from network api, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        standingRepository.setShouldReturnNetworkError(true)
        viewModel.refreshStandingsFromSpecificLeague("leagueId1")
        val value = viewModel.standingsStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
}