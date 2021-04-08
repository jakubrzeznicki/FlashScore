package com.kuba.flashscore.ui.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.network.FakeApiFootballService
import com.kuba.flashscore.launchFragmentInHiltContainer
import com.kuba.flashscore.other.Constants.COUNTRY_DATA_FILENAME
import com.kuba.flashscore.repositories.FakeCountryRepositoryAndroidTest
import com.kuba.flashscore.ui.TestFlashScoreFragmentFactory
import com.kuba.flashscore.util.JsonUtilAndroid
import com.kuba.flashscore.util.MockServerDispatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
//@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
class CountryFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: TestFlashScoreFragmentFactory


    private lateinit var mockWebServer: MockWebServer

    //
    @Inject
    lateinit var jsonUtilAndroid: JsonUtilAndroid

    @Inject
    lateinit var apiFootballService: FakeApiFootballService

    @Inject
    lateinit var countryRepository: FakeCountryRepositoryAndroidTest

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
    fun clickCountryItem_navigateToLeagueFragment() = runBlockingTest {
        val navController = mock(NavController::class.java)
        val countryItem = Country("id", "logo", "name")
        var testViewModel: CountryViewModel? = null

        val apiService = configureFakeApiFootballService(
            countriesDataSource = COUNTRY_DATA_FILENAME,
            networkDelay = 0L
        )
        val repository = configureFakeRepository(apiService)

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            testViewModel = viewModel
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
        assertThat(testViewModel?.countries?.getOrAwaitValue()).contains(
            countryItem
        )
    }

    @Test
    fun clickCountryItemFetchedFromNetwork_navigateToLeagueFragment() {
        //Launch fragment
        val navController = mock(NavController::class.java)
        //  val testViewModel = CountryViewModel(FakeCountryRepositoryAndroidTest())

        launchFragmentInHiltContainer<CountryFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            //countryAdapter.country = testViewModel.countries.value?.peekContent()?.data?.toList()!!
        }
        //Click on first article
        onView(withId(R.id.recyclerViewCountries))
            .perform(actionOnItemAtPosition<CountryAdapter.CountryViewHolder>(0, click()))

        //Check that it navigates to Detail screen
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.leagueFragment)
    }


    @Test
    fun testtt() {
        val apiService = configureFakeApiFootballService(
            countriesDataSource = COUNTRY_DATA_FILENAME,
            networkDelay = 0L
        )
        configureFakeRepository(apiService)
    }

    fun configureFakeApiFootballService(
        countriesDataSource: String? = null,
        networkDelay: Long? = null
    ): FakeApiFootballService {
        val fakeApiService = apiFootballService
        countriesDataSource?.let { fakeApiService.countryyJsonFileName = it }
        networkDelay?.let { fakeApiService.networkDelay = it }
        return fakeApiService
    }

    fun configureFakeRepository(
        apiFootballService: FakeApiFootballService
    ): FakeCountryRepositoryAndroidTest {
        val fakeRepository = countryRepository
        fakeRepository.apiFootballService = apiFootballService
        return fakeRepository
    }
}