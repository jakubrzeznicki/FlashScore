package com.kuba.flashscore.ui.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.FakeCountryRepositoryAndroidTest
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.events.details.EventDetailsViewPagerFragment
import com.kuba.flashscore.ui.events.details.EventDetailsViewPagerFragmentArgs
import com.kuba.flashscore.util.DataProducer
import com.kuba.flashscore.util.MatcherUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.wait
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
class CountryFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickCountryItemFromDatabase_navigateToLeagueFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)
        val countryItem = DataProducer.produceCountryEntity(1)
        val testViewModel = CountryViewModel(FakeCountryRepositoryAndroidTest().also {
            it.insertCountries(
                listOf(countryItem)
            )
        }, FakeConnectivityManager().also { it.isNetworkAvailable.postValue(true) })

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            //viewModel = testViewModel
            countryAdapter.country = listOf(countryItem.asDomainModel())
            countryAdapter
        }


        onView(withId(R.id.recyclerViewCountries))
            .perform(
                actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            CountryFragmentDirections.actionCountryFragmentToLeagueFragment(
                countryItem.asDomainModel()
            )
        )

        testViewModel.getCountries()

        assertThat(testViewModel.countries.getOrAwaitValue()).contains(
            countryItem.asDomainModel()
        )
    }

    @Test
    fun clickCountryItemFromNetwork_navigateToLeagueFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)
        val countryItem = DataProducer.produceCountryEntity(1)
        val testViewModel = CountryViewModel(
            FakeCountryRepositoryAndroidTest(),
            FakeConnectivityManager().also { it.isNetworkAvailable.postValue(true) })

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            countryAdapter.country = listOf(countryItem.asDomainModel())

        }

        onView(withId(R.id.recyclerViewCountries))
            .perform(
                actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            CountryFragmentDirections.actionCountryFragmentToLeagueFragment(
                countryItem.asDomainModel()
            )
        )

        testViewModel.refreshCountries()
        assertThat(testViewModel.countriesStatus.getOrAwaitValue().getContentIfNotHandled()?.status).isEqualTo(
            Status.SUCCESS
        )
    }
//
//    @Test
//    fun networkIsNotAvailable_showSnackBarWithNetworkErrorInformation() = runBlockingTest {
//        val navController = mock(NavController::class.java)
//        val testViewModel = CountryViewModel(
//            FakeCountryRepositoryAndroidTest(),
//            FakeConnectivityManager().also { it.isNetworkAvailable.postValue(false) })
//
//        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
//            Navigation.setViewNavController(requireView(), navController)
//            viewModel = testViewModel
//            countryAdapter.country = emptyList()
//        }
//
//        testViewModel.refreshCountries()
//        delay(3000)
////        assertThat(testViewModel.countriesStatus.getOrAwaitValue().peekContent().status).isEqualTo(
////            Status.ERROR
////        )
//        assertThat(testViewModel.countriesStatus.getOrAwaitValue().peekContent().message).isEqualTo(
//            ERROR_MESSAGE
//        )
//    }
}