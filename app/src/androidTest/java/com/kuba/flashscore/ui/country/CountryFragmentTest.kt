package com.kuba.flashscore.ui.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.repositories.FakeFlashScoreRepositoryAndroidTest
import com.kuba.flashscore.ui.FlashScoreFragmentFacotry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
    lateinit var fragmentFactory: FlashScoreFragmentFacotry

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickCountryItem_navigateToLeagueFragment() {
        val navController = mock(NavController::class.java)
        val countryItem = CountryDto("id", "logo", "name")
        val testViewModel = CountryViewModel(FakeFlashScoreRepositoryAndroidTest())

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            countryAdapter.country = listOf(countryItem)
        }

        onView(withId(R.id.recyclerViewCountries))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            CountryFragmentDirections.actionCountryFragmentToLeagueFragment(
                countryItem
            )
        )
//        assertThat(testViewModel?.countries?.getOrAwaitValue()?.peekContent()?.data?.toList()).contains(
//            countryItem
//        )
    }
}