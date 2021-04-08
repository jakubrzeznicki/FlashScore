package com.kuba.flashscore.ui.country

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.country.FakeCountryRepository
import com.kuba.flashscore.ui.util.networking.FakeConnectivityManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class CountryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CountryViewModel
    lateinit var countryRepository: FakeCountryRepository
    lateinit var connectivityManager: FakeConnectivityManager


    @Before
    fun setup() {
        countryRepository = FakeCountryRepository()
        connectivityManager = FakeConnectivityManager()
        val country1 = CountryEntity("countryId1", "countryLogo1", "countryName1")
        val country2 = CountryEntity("countryId2", "countryLogo2", "countryName2")
        val country3 = CountryEntity("countryId3", "countryLogo3", "countryName3")
        runBlockingTest {
            countryRepository.insertCountries(listOf(country1, country2, country3))
        }
        viewModel = CountryViewModel(countryRepository, connectivityManager)
    }


    @Test
    fun `insert country item and get from database, return success`() {
        viewModel.getCountries()

        val value = viewModel.countries.getOrAwaitValueTest()

        assertThat(value).isNotEmpty()
        assertThat(value).hasSize(3)
    }

    @Test
    fun `fetch correct data from network api, returns success`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        viewModel.refreshCountries()
        val value = viewModel.countriesStatus.getOrAwaitValueTest()

        assertThat(value.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(value.peekContent().data).isNotEmpty()
        assertThat(value.peekContent().data).hasSize(3)
    }

    @Test
    fun `fetch incorrect data from network api, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(true)
        countryRepository.setShouldReturnNetworkError(true)
        viewModel.refreshCountries()
        val value = viewModel.countriesStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `network is not available, returns error`() = runBlocking {
        connectivityManager.setNetworkAvailable(false)
        countryRepository.setShouldReturnNetworkError(true)
        viewModel.refreshCountries()
        val value = viewModel.countriesStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
        assertThat(value.peekContent().message).isEqualTo(Constants.ERROR_MESSAGE)

    }
}