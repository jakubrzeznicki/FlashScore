package com.kuba.flashscore.repositories.standing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.daos.StandingDao
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.local.models.entities.StandingType
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.models.CountryDto
import com.kuba.flashscore.data.network.models.StandingDto
import com.kuba.flashscore.data.network.responses.CountryResponse
import com.kuba.flashscore.data.network.responses.StandingResponse
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mockito.mock
import retrofit2.Response
import timber.log.Timber
import javax.annotation.Resource

@ExperimentalCoroutinesApi
class DefaultStandingRepositoryTest {

    //Test subject
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var standingRepository: StandingRepository

    //Collaborators
    private lateinit var standingDao: StandingDao
    private lateinit var footballApiService: ApiFootballService

    //Utilities
    lateinit var standingFromApi: Response<StandingResponse>
    lateinit var standingsFromDao: StandingEntity

    @Before
    fun setup() = runBlockingTest {
        //mocking dao
        standingDao = mock()
        standingsFromDao = StandingEntity(
            "leagueRound1",
            "leagueD1",
            "leagueGA1",
            "leagueGF1",
            "leagueL1",
            "leaguePTS1",
            "leaguePayed1",
            "leaguePosition1",
            "leagueW1",
            "promotion1",
            "teamId1",
            "leagueId1",
            StandingType.OVERALL
        )
        whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(
            listOf(
                standingsFromDao
            )
        )

        whenever(
            standingDao.getStandingsFromSpecificLeague(
                "leagueId1",
                StandingType.OVERALL
            )
        ).thenReturn(
            listOf(
                standingsFromDao
            )
        )

        //mocking api
        footballApiService = mock()
        standingFromApi = produceStandingResponseSuccess()
        whenever(footballApiService.getStandings("leagueId1")).thenReturn(standingFromApi)

        standingRepository = DefaultStandingRepository(standingDao, footballApiService)
    }

    @Test
    fun getStandingsFromDatabase_shouldReturnStandings() = runBlockingTest {
        standingRepository.getStandingsFromSpecificLeagueFromDb("leagueId1", StandingType.OVERALL)
        standingRepository.getAllStandingsFromSpecificLeagueFromDb("leagueId1")
        verify(standingDao, times(1)).getStandingsFromSpecificLeague(
            "leagueId1",
            StandingType.OVERALL
        )
        verify(standingDao, times(1)).getAllStandingsFromSpecificLeague("leagueId1")
    }


    @Test
    fun getStandingsFromDatabaseAndStandingsExists_shouldNotCallToApiService() = runBlockingTest {
        standingRepository.getAllStandingsFromSpecificLeagueFromDb(any())
        verify(footballApiService, never()).getCountries()
    }


    @Test
    fun ifDaoReturnsAllStandingsThenRepositoryReturnsSameStandings() = runBlockingTest {
        val standings = standingRepository.getAllStandingsFromSpecificLeagueFromDb("leagueId1")
        assertThat(standings).hasSize(1)
        assertThat(standings).contains(standingsFromDao)
        assertThat(standings[0]).isEqualTo(standingsFromDao)
    }

    @Test
    fun ifDaoReturnsSpecificStandingsThenRepositoryReturnsSameStandings() = runBlockingTest {
        val standings = standingRepository.getStandingsFromSpecificLeagueFromDb(
            "leagueId1",
            StandingType.OVERALL
        )
        assertThat(standings).hasSize(1)
        assertThat(standings[0].standingType).isEqualTo(StandingType.OVERALL)
        assertThat(standings).contains(standingsFromDao)
        assertThat(standings[0]).isEqualTo(standingsFromDao)
    }

    @Test
    fun getStandingsFromDatabaseIsEmpty_shouldCallToApiService() = runBlockingTest {
        whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(null)
        whenever(
            standingDao.getStandingsFromSpecificLeague(
                "leagueId1",
                StandingType.OVERALL
            )
        ).thenReturn(null)

        val standings = standingRepository.refreshStandingsFromSpecificLeague("leagueId1")

        verify(footballApiService, times(1)).getStandings("leagueId1")
        verify(standingDao, times(1)).getAllStandingsFromSpecificLeague("leagueId1")
    }


    @Test
    fun apiServiceIsCallBeforeCallStandingDaoIfDatabaseIsEmpty() = runBlockingTest {
        whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(null)

        standingRepository.refreshStandingsFromSpecificLeague("leagueId1")

        val orderVerifier: InOrder = inOrder(footballApiService, standingDao)
        orderVerifier.verify(footballApiService).getStandings("leagueId1")
        orderVerifier.verify(standingDao)
            .getAllStandingsFromSpecificLeague("leagueId1")
    }

    @Test
    fun refreshCountriesFromNetwork_countriesShouldAlsoInsertToDatabase() = runBlockingTest {
        whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(null)
        standingRepository.refreshStandingsFromSpecificLeague("leagueId1")
        verify(standingDao, times(3)).insertStandings(any())
        whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(listOf(standingsFromDao))

        val standings = standingRepository.getAllStandingsFromSpecificLeagueFromDb("leagueId1")
        assertThat(standings).hasSize(1)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeError_returnError() = runBlockingTest {
        whenever(footballApiService.getStandings("leagueId1")).thenReturn(
            produceStandingResponseError()
        )
        val standings = standingRepository.refreshStandingsFromSpecificLeague("leagueId1")

        verify(standingDao, never()).getAllStandingsFromSpecificLeague("leagueId1")

        verify(footballApiService, times(1)).getStandings("leagueId1")
        assertThat(standings.status).isEqualTo(Status.ERROR)
        assertThat(standings.data).isEqualTo(null)
        assertThat(standings.message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeErrorAndDatabaseIsAlsoEmpty_returnError() =
        runBlockingTest {
            whenever(footballApiService.getStandings("leagueId1")).thenReturn(
                produceStandingResponseError()
            )
            whenever(standingDao.getAllStandingsFromSpecificLeague("leagueId1")).thenReturn(null)
            standingRepository.refreshStandingsFromSpecificLeague("leagueId1")

            val standingsFromDatabase =
                standingRepository.getAllStandingsFromSpecificLeagueFromDb("leagueId1")

            verify(standingDao, times(1)).getAllStandingsFromSpecificLeague("leagueId1")
            verify(footballApiService, times(1)).getStandings("leagueId1")
            assertThat(standingsFromDatabase).isNull()
        }

    @Test
    fun insertStandingsIntoDb_shouldReturnSuccess() = runBlockingTest {
        StandingEntity(
            "leagueRound1",
            "leagueD1",
            "leagueGA1",
            "leagueGF1",
            "leagueL1",
            "leaguePTS1",
            "leaguePayed1",
            "leaguePosition1",
            "leagueW1",
            "promotion1",
            "teamId1",
            "leagueId1",
            StandingType.OVERALL
        )
        val standingList = listOf<StandingEntity>(
            StandingEntity(
                "leagueRound3",
                "leagueD3",
                "leagueGA3",
                "leagueGF3",
                "leagueL3",
                "leaguePTS3",
                "leaguePayed3",
                "leaguePosition3",
                "leagueW3",
                "promotion3",
                "teamId3",
                "leagueId3",
                StandingType.OVERALL
            ),
            StandingEntity(
                "leagueRound4",
                "leagueD4",
                "leagueGA4",
                "leagueGF4",
                "leagueL4",
                "leaguePTS4",
                "leaguePayed4",
                "leaguePosition4",
                "leagueW4",
                "promotion4",
                "teamId4",
                "leagueId4",
                StandingType.OVERALL
            )
        )

        standingRepository.insertStandings(standingList)
        verify(standingDao, times(1)).insertStandings(standingList)
    }

    private fun produceStandingResponseSuccess(): Response<StandingResponse> {
        val standingResponse = StandingResponse()
        standingResponse.add(
            0, StandingDto(
                "awayLeagueD2",
                "awayLeagueGA2",
                "awayLeagueGF2",
                "awayLeagueL2",
                "awayLeaguePTS2",
                "awayLeaguePayed2",
                "awayLeaguePosition2",
                "awayLeagueW2",
                "awayPromotion2",
                "countryName2",
                "homeLeagueD2",
                "homeLeagueGA2",
                "homeLeagueGF2",
                "homeLeagueL2",
                "homeLeaguePTS2",
                "homeLeaguePayed2",
                "homeLeaguePosition2",
                "homeLeagueW2",
                "homePromotion2",
                "leagueId1",
                "leagueName2",
                "leagueRound2",
                "overallLeagueD2",
                "overallLeagueGA2",
                "overallLeagueGF2",
                "overallLeagueL2",
                "overallLeaguePTS2",
                "overallLeaguePayed2",
                "overallLeaguePosition2",
                "overallLeagueW2",
                "overallPromotion2",
                "teamBadge2",
                "teamId2",
                "teamName2"

            )
        )
        return Response.success(standingResponse)
    }

    private fun produceStandingResponseError(): Response<StandingResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }
}