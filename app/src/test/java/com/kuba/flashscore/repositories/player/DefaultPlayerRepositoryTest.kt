package com.kuba.flashscore.repositories.player

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.models.*
import com.kuba.flashscore.data.network.responses.CountryResponse
import com.kuba.flashscore.data.network.responses.PlayerResponse
import com.kuba.flashscore.data.network.responses.StandingResponse
import com.kuba.flashscore.data.network.responses.TeamResponse
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
class DefaultPlayerRepositoryTest {

    //Test subject
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var playerRepository: PlayerRepository

    //Collaborators
    private lateinit var playerDao: PlayerDao
    private lateinit var footballApiService: ApiFootballService

    //Utilities
    lateinit var playersFromApi: Response<PlayerResponse>
    lateinit var playerFromDao: PlayerEntity

    lateinit var teamFromDao: TeamEntity
    lateinit var playersFromDao: List<PlayerEntity>
    lateinit var coachesFromDao: List<CoachEntity>
    lateinit var teamWithPlayersAndCoachFromDao: TeamWithPlayersAndCoachEntity


    @Before
    fun setup() = runBlockingTest {
        //mocking dao
        playerDao = mock()
        playerFromDao = PlayerEntity(
            "teamId2",
            "playerAge1",
            "playerCountry1",
            "playerGoals1",
            1L,
            "playerMatchPlayed1",
            "playerName1",
            "playerNumber1",
            "playerRedCard1",
            "playerType1",
            "playerYellowCard1"
        )

        teamFromDao = TeamEntity(
            "leagueId1",
            "teamBadge1",
            "teamId1",
            "teamName1",

            )
        playersFromDao = producePlayerEntities()
        coachesFromDao = produceCoachEntities()

        teamWithPlayersAndCoachFromDao = TeamWithPlayersAndCoachEntity(
            teamFromDao,
            playersFromDao,
            coachesFromDao
        )

        whenever(playerDao.getAllPlayers()).thenReturn(
            listOf(playerFromDao)
        )
        whenever(playerDao.getPlayersByPlayerId(any())).thenReturn(
            playerFromDao
        )
        whenever(playerDao.getPlayersFromSpecificTeam(any())).thenReturn(
            teamWithPlayersAndCoachFromDao
        )

        //mocking api
        footballApiService = mock()
        playersFromApi = producePlayerResponseSuccess()
        whenever(footballApiService.getPlayer("playerName1")).thenReturn(playersFromApi)

        playerRepository = DefaultPlayerRepository(playerDao, footballApiService)
    }

    @Test
    fun getPlayerInformationFromDatabase_shouldReturnTeams() = runBlockingTest {
        playerRepository.getPlayerInformationFromDb(1L)

        verify(playerDao, times(1)).getPlayersByPlayerId(1L)
    }

    @Test
    fun getPlayersFromSpecificTeamFromDatabase_shouldReturnTeams() = runBlockingTest {
        playerRepository.getPlayersFromSpecificTeamFromDb("teamId1")

        verify(playerDao, times(1)).getPlayersFromSpecificTeam("teamId1")
    }

    @Test
    fun getPlayerInformationFromDatabaseAndPlayerExists_shouldNotCallToApiService() = runBlockingTest {
        playerRepository.getPlayerInformationFromDb(1L)
        verify(footballApiService, never()).getPlayer("playerName1")
    }




    private fun producePlayerResponseSuccess(): Response<PlayerResponse> {
        val playerResponse = PlayerResponse()
        playerResponse.add(
            0,
            PlayerInfoDto(
                "playerAge2",
                "playerCountry2",
                "playerGoals2",
                2L,
                "playerMatchPlayed2",
                "playerName2",
                "playerNumber2",
                "playerRedCard2",
                "playerType2",
                "playerYellowCard2",
                "teamKey1",
                "teamName1"
            )
        )
        return Response.success(playerResponse)
    }

    private fun producePlayerResponseError(): Response<PlayerResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }

    private fun producePlayerEntities(): List<PlayerEntity> {
        return listOf(
            PlayerEntity(
                "teamId2",
                "playerAge1",
                "playerCountry1",
                "playerGoals1",
                1L,
                "playerMatchPlayed1",
                "playerName1",
                "playerNumber1",
                "playerRedCard1",
                "playerType1",
                "playerYellowCard1"
            )
        )
    }

    private fun produceCoachEntities(): List<CoachEntity> {
        return listOf(
            CoachEntity(
                "teamId2",
                "coachAge1",
                "coachCountry1",
                "coachName1"
            )
        )
    }
}