package com.kuba.flashscore.ui.league

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.country.FakeCountryRepository
import com.kuba.flashscore.repositories.league.FakeLeagueRepository
import com.kuba.flashscore.ui.util.networking.FakeConnectivityManager
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class LeagueViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LeagueViewModel
    lateinit var leagueRepository: FakeLeagueRepository
    lateinit var connectivityManager: FakeConnectivityManager

    @Before
    fun setup() {
        leagueRepository = FakeLeagueRepository()
        connectivityManager = FakeConnectivityManager()

        val league1 = produceLeagueEntity(1, 1)
        val league2 = produceLeagueEntity(2, 1)
        val league3 = produceLeagueEntity(3, 1)

        runBlockingTest {
            leagueRepository.insertLeagues(listOf(league1, league2, league3))
        }
        viewModel = LeagueViewModel(leagueRepository, connectivityManager)
    }


    @Test
    fun `insert league item and get from database, return success`() {
        viewModel.getCountryWithLeagues("countryId1")

        val value = viewModel.countriesWithLeagues.getOrAwaitValueTest()

        assertThat(value.leagues).isNotEmpty()
        assertThat(value.leagues).hasSize(3)
    }

    @Test
    fun `fetch correct data from network api, returns success`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        viewModel.refreshCountryWithLeagues("countryId1")
        val value = viewModel.countryWithLeaguesStatus.getOrAwaitValueTest()

        assertThat(value.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(value.peekContent().data?.leagues).isNotEmpty()
        assertThat(value.peekContent().data?.leagues).hasSize(3)
    }

    @Test
    fun `fetch incorrect data from network api, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        leagueRepository.setShouldReturnNetworkError(true)
        viewModel.refreshCountryWithLeagues("countryId1")
        val value = viewModel.countryWithLeaguesStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `network is not available, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(false)
        leagueRepository.setShouldReturnNetworkError(true)
        viewModel.refreshCountryWithLeagues("countryId1")
        val value = viewModel.countryWithLeaguesStatus.getOrAwaitValueTest()

        assertThat(value.peekContent().status).isEqualTo(Status.ERROR)
        assertThat(value.peekContent().message).isEqualTo(Constants.ERROR_MESSAGE)
    }
}