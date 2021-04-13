package com.kuba.flashscore.ui.league

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.LeagueAdapter
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.FakeCountryRepositoryAndroidTest
import com.kuba.flashscore.repositories.FakeLeagueRepositoryAndroidTest
import com.kuba.flashscore.ui.FakeConnectivityManager
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.ui.league.LeagueFragment
import com.kuba.flashscore.ui.league.LeagueFragmentArgs
import com.kuba.flashscore.ui.league.LeagueFragmentDirections
import com.kuba.flashscore.ui.league.LeagueViewModel
import com.kuba.flashscore.util.DataProducer
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.wait
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import timber.log.Timber
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class LeagueFragmentTest {

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
    fun clickLeagueItemFromDatabase_navigateToEventListFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)
        val leagueItem = produceLeagueEntity(1, 1)
        val countryAndLeagueItems = CountryAndLeaguesEntity(
            produceCountryEntity(1),
            listOf(leagueItem)
        )

        val testViewModel = LeagueViewModel(FakeLeagueRepositoryAndroidTest().also {
            it.insertLeagues(
                listOf(leagueItem)
            )
        }, FakeConnectivityManager().also {
            it.isNetworkAvailable.postValue(true)
        })

        val bundle = LeagueFragmentArgs(
            produceCountryEntity(1).asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<LeagueFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel

            leagueAdapter.league = listOf(leagueItem.asDomainModel())
        }

        onView(withId(R.id.recyclerViewLeagues))
            .perform(
                actionOnItemAtPosition<LeagueAdapter.LeagueViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            LeagueFragmentDirections.actionLeagueFragmentToEventsListFragment(
                countryAndLeagueItems.asDomainModel()
            )
        )

        testViewModel.getCountryWithLeagues("countryId1")

        assertThat(testViewModel.countriesWithLeagues.getOrAwaitValue()).isEqualTo(
            countryAndLeagueItems.asDomainModel()
        )
    }

    @Test
    fun clickLeagueItemFromNetwork_navigateToEventListFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)
        val leagueItem = produceLeagueEntity(1, 1)
        val countryAndLeagueItems = CountryAndLeaguesEntity(
            produceCountryEntity(1),
            listOf(leagueItem)
        )

        val testViewModel = LeagueViewModel(FakeLeagueRepositoryAndroidTest().also {
            it.insertLeagues(
                listOf(leagueItem)
            )
        }, FakeConnectivityManager().also {
            it.isNetworkAvailable.postValue(true)
        })

        val bundle = LeagueFragmentArgs(
            produceCountryEntity(1).asDomainModel()
        ).toBundle()

        launchFragmentInHiltContainer<LeagueFragment>(
            fragmentFactory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            navController.setGraph(R.navigation.nav_graph, bundle)
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel

            leagueAdapter.league = listOf(leagueItem.asDomainModel())
        }

        onView(withId(R.id.recyclerViewLeagues))
            .perform(
                actionOnItemAtPosition<LeagueAdapter.LeagueViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            LeagueFragmentDirections.actionLeagueFragmentToEventsListFragment(
                countryAndLeagueItems.asDomainModel()
            )
        )


        testViewModel.refreshCountryWithLeagues("countryId1")

        assertThat(
            testViewModel.countryWithLeaguesStatus.getOrAwaitValue()
                .getContentIfNotHandled()?.status
        ).isEqualTo(
            Status.SUCCESS
        )
    }

}

fun waitFor(delay: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "wait for " + delay + "milliseconds"
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}