package com.kuba.flashscore.ui.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.FakeFlashScoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class CountryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CountryViewModel
    lateinit var flashScoreRepository: FakeFlashScoreRepository


    @Before
    fun setup() {
        flashScoreRepository = FakeFlashScoreRepository()
        viewModel = CountryViewModel(flashScoreRepository)
    }

    //private val server: MockWebServer = MockWebServer()
   // private val MOCK_WEBSERVER_PORT = 8000

   // lateinit var apiFootballService: ApiFootballService

//    @Before
//    fun init() {
        //server.start(MOCK_WEBSERVER_PORT)

//        apiFootballService = Retrofit.Builder()
//            .baseUrl(server.url("/"))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(Gson()))
//            .build()
//            .create(ApiFootballService::class.java)
//
//        flashScoreRepository = FakeFlashScoreRepository()
   // }

//    @After
//    fun shutdown() {
//        server.shutdown()
//    }

    @Test
    fun `fetch correct data from network api, returns success`() {
//        server.apply {
//            enqueue(MockResponse().setBody(MockResponseFileReader("success_country.json").content))
//            viewModel.getCountries()
//        }

        viewModel.getCountries()
        val value = viewModel.countries.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `fetch incorrect data from network api, returns error`() {
//        server.apply {
//            enqueue(MockResponse().setBody(MockResponseFileReader("error.json").content))
//            flashScoreRepository.
//            viewModel.getCountries()
//        }
        flashScoreRepository.setShouldReturnNetworkError(true)
        viewModel.getCountries()
        val value = viewModel.countries.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
}