package com.kuba.flashscore.ui.teams.standings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.kuba.flashscore.R
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class StandingsViewPagerFragmentTest {

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
    fun tabLayoutAndViewPagerIsDisplayedCorrectly() = runBlockingTest {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<StandingsViewPagerFragment>(
            fragmentFactory = fragmentFactory
        ) {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.viewPagerStandings)).check(matches(isDisplayed()))
        onView(withId(R.id.tabLayoutStandings2)).check(matches(isDisplayed()))
    }
}