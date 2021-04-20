package com.kuba.flashscore.repositories.league

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.daos.LeagueDao
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.models.LeagueDto
import com.kuba.flashscore.data.network.responses.LeagueResponse
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InOrder
import retrofit2.Response

@ExperimentalCoroutinesApi
class DefaultLeagueRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var leagueRepository: LeagueRepository

    private lateinit var leagueDao: LeagueDao
    private lateinit var apiFootballService: ApiFootballService

    lateinit var leagueFromApi: Response<LeagueResponse>
    lateinit var allLeagueFromDao: LeagueEntity
    lateinit var leaguesFromSpecificCountryFromDao: CountryAndLeaguesEntity
    lateinit var country: CountryEntity
    lateinit var leagues: List<LeagueEntity>

    @Before
    fun setup() = runBlockingTest {
        leagueDao = mock()
        allLeagueFromDao = produceLeagueEntity(1, 1)

        whenever(leagueDao.getAllLeagues()).thenReturn(listOf(allLeagueFromDao))

        country = produceCountryEntity(3)
        leagues = listOf(produceLeagueEntity(3, 3))
        leaguesFromSpecificCountryFromDao = CountryAndLeaguesEntity(
            country,
            leagues
        )
        whenever(leagueDao.getLeaguesFromSpecificCountry(any())).thenReturn(
            leaguesFromSpecificCountryFromDao
        )

        apiFootballService = mock()
        leagueFromApi = produceLeagueResponseSuccess()
        whenever(apiFootballService.getLeagues("countryId2")).thenReturn(leagueFromApi)

        leagueRepository = DefaultLeagueRepository(leagueDao, apiFootballService)
    }

    @Test
    fun getAllLeaguesFromDatabase_shouldReturnLeagues() = runBlockingTest {
        leagueRepository.getLeaguesFromDb()
        verify(leagueDao, times(1)).getAllLeagues()
    }

    @Test
    fun getLeaguesFromSpecificCountryFromDatabase_shouldReturnLeagues() = runBlockingTest {
        leagueRepository.getLeagueFromSpecificCountryFromDb("countryId3")
        verify(leagueDao, times(1)).getLeaguesFromSpecificCountry("countryId3")
    }

    @Test
    fun getAllLeaguesFromDatabaseAndLeaguesExists_shouldNotCallToApiService() = runBlockingTest {
        leagueRepository.getLeaguesFromDb()
        verify(apiFootballService, never()).getLeagues("countryId2")
    }

    @Test
    fun getLeaguesFromSpecificCountryFromDatabaseAndLeaguesExists_shouldNotCallToApiService() =
        runBlockingTest {
            leagueRepository.getLeagueFromSpecificCountryFromDb("countryId2")
            verify(apiFootballService, never()).getLeagues("countryId2")
        }

    @Test
    fun ifDaoReturnsAllLeaguesThenRepositoryReturnsSameLeagues() = runBlockingTest {
        val leagues = leagueRepository.getLeaguesFromDb()
        assertThat(leagues).hasSize(1)
        assertThat(leagues).contains(allLeagueFromDao)
        assertThat(leagues[0].countryId).isEqualTo("countryId1")
        assertThat(leagues[0].leagueId).isEqualTo("leagueId1")
        assertThat(leagues[0].leagueName).isEqualTo("leagueName1")
    }

    @Test
    fun ifDaoReturnsLeaguesFromSpecificCountryThenRepositoryReturnsSameLeagues() = runBlockingTest {
        val leaguesFromRepo = leagueRepository.getLeagueFromSpecificCountryFromDb("countryId3")
        assertThat(leaguesFromRepo.leagues).hasSize(1)
        assertThat(leaguesFromRepo.leagues).isEqualTo(leagues)
        assertThat(leaguesFromRepo.country).isEqualTo(country)
    }

    @Test
    fun getLeaguesFromDatabaseIsEmpty_shouldCallToApiService() = runBlockingTest {
        whenever(leagueDao.getAllLeagues()).thenReturn(null)
        whenever(leagueDao.getLeaguesFromSpecificCountry("countryId2")).thenReturn(null)
        leagueRepository.refreshLeaguesFromSpecificCountry("countryId2")

        verify(leagueDao, times(1)).getLeaguesFromSpecificCountry("countryId2")
        verify(apiFootballService, times(1)).getLeagues("countryId2")
    }

    @Test
    fun apiServiceIsCallBeforeCallLeagueDaoIfDatabaseIsEmpty() = runBlockingTest {
        whenever(leagueDao.getAllLeagues()).thenReturn(null)
        whenever(leagueDao.getLeaguesFromSpecificCountry("countryId2")).thenReturn(null)
        leagueRepository.refreshLeaguesFromSpecificCountry("countryId2")

        val orderVerifier: InOrder = inOrder(apiFootballService, leagueDao)
        orderVerifier.verify(apiFootballService).getLeagues("countryId2")
        orderVerifier.verify(leagueDao).getLeaguesFromSpecificCountry("countryId2")
    }

    @Test
    fun refreshLeaguesFromNetwork_leaguesShouldAlsoInsertToDatabase() = runBlockingTest {
        whenever(leagueDao.getLeaguesFromSpecificCountry("countryId2")).thenReturn(null)
        leagueRepository.refreshLeaguesFromSpecificCountry("countryId2")

        verify(leagueDao, times(1)).insertLeagues(any())

        whenever(leagueDao.getLeaguesFromSpecificCountry("countryId2")).thenReturn(
            leaguesFromSpecificCountryFromDao
        )

        val leagues = leagueRepository.getLeagueFromSpecificCountryFromDb("countryId2")
        assertThat(leagues.leagues).hasSize(1)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeError_returnError() = runBlockingTest {
        whenever(apiFootballService.getLeagues("countryId2")).thenReturn(produceLeagueResponseError())
        val leagues = leagueRepository.refreshLeaguesFromSpecificCountry("countryId2")

        verify(leagueDao, never()).getLeaguesFromSpecificCountry("countryId2")

        verify(apiFootballService, times(1)).getLeagues("countryId2")
        assertThat(leagues.status).isEqualTo(Status.ERROR)
        assertThat(leagues.data).isEqualTo(null)
        assertThat(leagues.message).isEqualTo(Constants.ERROR_MESSAGE)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeErrorAndDatabaseIsAlsoEmpty_returnError() =
        runBlockingTest {
            whenever(apiFootballService.getLeagues("countryId2")).thenReturn(
                produceLeagueResponseError()
            )
            whenever(leagueDao.getLeaguesFromSpecificCountry("countryId2")).thenReturn(null)
            leagueRepository.refreshLeaguesFromSpecificCountry("countryId2")

            val specificLeaguesFromDatabase =
                leagueRepository.getLeagueFromSpecificCountryFromDb("countryId2")

            verify(leagueDao, times(1)).getLeaguesFromSpecificCountry("countryId2")
            verify(apiFootballService, times(1)).getLeagues("countryId2")
            assertThat(specificLeaguesFromDatabase).isNull()
        }

    private fun produceLeagueResponseSuccess(): Response<LeagueResponse> {
        val leagueResponse = LeagueResponse()
        leagueResponse.add(
            0,
            LeagueDto(
                "countryId2",
                "countryLogo2",
                "countryName2",
                "leagueId2",
                "leagueLogo2",
                "leagueName2",
                "leagueSeason2"
            )
        )
        return Response.success(leagueResponse)
    }

    private fun produceLeagueResponseError(): Response<LeagueResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }
}