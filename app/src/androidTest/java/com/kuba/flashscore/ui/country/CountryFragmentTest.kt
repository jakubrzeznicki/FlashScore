package com.kuba.flashscore.ui.country

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kuba.flashscore.HiltTestActivity
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.di.AppModule
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.repositories.FakeFlashScoreRepositoryAndroidTest
import com.kuba.flashscore.ui.FlashScoreFragmentFacotry
import com.kuba.flashscore.util.FileReader.readStringFromFile
import com.kuba.flashscore.util.MockServerDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CountryFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FlashScoreFragmentFacotry


    private lateinit var mockWebServer: MockWebServer
    @Inject
    lateinit var okHttp: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
       IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttp))
    }

    @After
    fun tearDown() =
        mockWebServer.shutdown()

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
                actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(
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

    @Test
    fun clickCountryItemFetchedFromNetwork_navigateToLeagueFragment() {
        //Launch fragment
        val navController = mock(NavController::class.java)
        val testViewModel = CountryViewModel(FakeFlashScoreRepositoryAndroidTest())

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
          //  countryAdapter.country = testViewModel.countries.value?.peekContent()?.data?.toList()!!
        }
        //Click on first article
        onView(withId(R.id.recyclerViewCountries))
            .perform(actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(0, click()))

        //Check that it navigates to Detail screen
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.leagueFragment)
    }
}