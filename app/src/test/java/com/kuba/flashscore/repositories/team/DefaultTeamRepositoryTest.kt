package com.kuba.flashscore.repositories.team

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
import com.kuba.flashscore.data.network.responses.StandingResponse
import com.kuba.flashscore.data.network.responses.TeamResponse
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.util.DataProducer.produceCoachEntity
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.DataProducer.producePlayerEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity
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
class DefaultTeamRepositoryTest {

    //Test subject
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var teamRepository: TeamRepository

    //Collaborators
    private lateinit var teamDao: TeamDao
    private lateinit var coachDao: CoachDao
    private lateinit var playerDao: PlayerDao
    private lateinit var footballApiService: ApiFootballService

    //Utilities
    lateinit var teamsFromApi: Response<TeamResponse>
    lateinit var teamsFromDao: TeamEntity
    lateinit var teamFromDao: TeamEntity
    lateinit var playersFromDao: List<PlayerEntity>
    lateinit var coachesFromDao: List<CoachEntity>
    lateinit var countryWithLeagueAndTeamsFromDao: CountryWithLeagueAndTeamsEntity
    lateinit var teamWithPlayersAndCoachFromDao: TeamWithPlayersAndCoachEntity

    // 1 teams from database, 2 - team from database, 3 - teams from network
    @Before
    fun setup() = runBlockingTest {
        //mocking dao
        playerDao = mock()
        coachDao = mock()
        teamDao = mock()
        teamsFromDao = produceTeamEntity(1, 1)
        teamFromDao = produceTeamEntity(2, 1)
        countryWithLeagueAndTeamsFromDao = CountryWithLeagueAndTeamsEntity(
            produceCountryEntity(1),
            listOf(
                LeagueWithTeamsEntity(
                    produceLeagueEntity(1, 1),
                    listOf(
                        teamsFromDao
                    )
                )
            )
        )

        whenever(teamDao.getTeamsFromSpecificLeague(any())).thenReturn(
            countryWithLeagueAndTeamsFromDao
        )

        playersFromDao = listOf(producePlayerEntity(1, 2))
        coachesFromDao = listOf(produceCoachEntity(1, 2))
        teamWithPlayersAndCoachFromDao = TeamWithPlayersAndCoachEntity(
            teamFromDao,
            playersFromDao,
            coachesFromDao
        )
        whenever(teamDao.getTeamByTeamId(any())).thenReturn(
            teamWithPlayersAndCoachFromDao
        )


        //mocking api
        footballApiService = mock()
        teamsFromApi = produceTeamResponseSuccess()
        whenever(footballApiService.getTeams("leagueId1")).thenReturn(teamsFromApi)

        teamRepository = DefaultTeamRepository(coachDao, playerDao, teamDao, footballApiService)
    }

    @Test
    fun getTeamsFromSpecificLeagueFromDatabase_shouldReturnTeams() = runBlockingTest {
        teamRepository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb("leagueId1")

        verify(teamDao, times(1)).getTeamsFromSpecificLeague("leagueId1")
    }

    @Test
    fun getTeamByTeamIdFromDatabase_shouldReturnTeams() = runBlockingTest {
        teamRepository.getTeamWithPlayersAndCoachFromDb("leagueId1")

        verify(teamDao, times(1)).getTeamByTeamId("leagueId1")
    }

    @Test
    fun getTeamsFromDatabaseAndTeamsExists_shouldNotCallToApiService() = runBlockingTest {
        teamRepository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb("leagueId1")
        verify(footballApiService, never()).getTeams("leagueId1")
    }

    @Test
    fun getTeamFromDatabaseAndTeamExists_shouldNotCallToApiService() = runBlockingTest {
        teamRepository.getTeamWithPlayersAndCoachFromDb("teamId2")
        verify(footballApiService, never()).getTeams("teamId2")
    }


    @Test
    fun ifDaoReturnsAllTeamsThenRepositoryReturnsSameTeams() = runBlockingTest {
        val teams =
            teamRepository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb("leagueId1")
        assertThat(teams.leagueWithTeamEntities[0].teams).hasSize(1)
        assertThat(teams).isEqualTo(countryWithLeagueAndTeamsFromDao)
    }

    @Test
    fun ifDaoReturnsSpecificTeamThenRepositoryReturnsSameTeam() = runBlockingTest {
        val team = teamRepository.getTeamWithPlayersAndCoachFromDb("teamId2")
        assertThat(team).isEqualTo(teamWithPlayersAndCoachFromDao)
    }

    @Test
    fun getTeamsFromDatabaseIsEmpty_shouldCallToApiService() = runBlockingTest {
        whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(null)

        val teams = teamRepository.refreshTeamsFromSpecificLeague("leagueId1")

        verify(footballApiService, times(1)).getTeams("leagueId1")
        verify(teamDao, times(1)).getTeamsFromSpecificLeague("leagueId1")
        verify(teamDao, times(1)).insertTeams(any())
    }


    @Test
    fun apiServiceIsCallBeforeCallTeamDaoIfDatabaseIsEmpty() = runBlockingTest {
        whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(null)

        teamRepository.refreshTeamsFromSpecificLeague("leagueId1")

        val orderVerifier: InOrder = inOrder(footballApiService, teamDao)
        orderVerifier.verify(footballApiService).getTeams("leagueId1")
        orderVerifier.verify(teamDao)
            .getTeamsFromSpecificLeague("leagueId1")
    }

    @Test
    fun apiServiceIsCallBeforeCallTeamDaoIfDatabaseIsEmptyAndDataAreInsertInCorrectWay() =
        runBlockingTest {
            whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(null)

            teamRepository.refreshTeamsFromSpecificLeague("leagueId1")

            val orderVerifier: InOrder = inOrder(footballApiService, teamDao, playerDao, coachDao)
            orderVerifier.verify(footballApiService).getTeams("leagueId1")
            orderVerifier.verify(teamDao).insertTeams(any())
            orderVerifier.verify(playerDao).insertPlayers(any())
            orderVerifier.verify(coachDao).insertCoaches(any())
            orderVerifier.verify(teamDao)
                .getTeamsFromSpecificLeague("leagueId1")
        }

    @Test
    fun refreshTeamsFromNetwork_teamsShouldAlsoInsertToDatabase() = runBlockingTest {
        whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(null)
        teamRepository.refreshTeamsFromSpecificLeague("leagueId1")
        verify(teamDao, times(1)).insertTeams(any())
        verify(playerDao, times(1)).insertPlayers(any())
        verify(coachDao, times(1)).insertCoaches(any())
        whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(
            countryWithLeagueAndTeamsFromDao
        )

        val teams =
            teamRepository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb("leagueId1")
        assertThat(teams.leagueWithTeamEntities[0].teams).hasSize(1)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeError_returnError() = runBlockingTest {
        whenever(footballApiService.getTeams("leagueId1")).thenReturn(
            produceTeamResponseError()
        )
        val teams = teamRepository.refreshTeamsFromSpecificLeague("leagueId1")

        verify(teamDao, never()).getTeamsFromSpecificLeague("leagueId1")

        verify(footballApiService, times(1)).getTeams("leagueId1")
        assertThat(teams.status).isEqualTo(Status.ERROR)
        assertThat(teams.data).isEqualTo(null)
        assertThat(teams.message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeErrorAndDatabaseIsAlsoEmpty_returnError() =
        runBlockingTest {
            whenever(footballApiService.getTeams("leagueId1")).thenReturn(
                produceTeamResponseError()
            )
            whenever(teamDao.getTeamsFromSpecificLeague("leagueId1")).thenReturn(null)
            teamRepository.refreshTeamsFromSpecificLeague("leagueId1")

            val standingsFromDatabase =
                teamRepository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb("leagueId1")

            verify(teamDao, times(1)).getTeamsFromSpecificLeague("leagueId1")
            verify(footballApiService, times(1)).getTeams("leagueId1")
            assertThat(standingsFromDatabase).isNull()
        }

    @Test
    fun insertTeamsPlayersAndCoachesIntoDb_shouldReturnSuccess() = runBlockingTest {
        val team = produceTeamEntity(1, 1)

        teamRepository.insertTeams(listOf(team))
        teamRepository.insertPlayers(listOf(producePlayerEntity(1, 2)))
        teamRepository.insertCoaches(listOf(produceCoachEntity(1, 2)))

        verify(teamDao, times(1)).insertTeams(any())
        verify(playerDao, times(1)).insertPlayers(any())
        verify(coachDao, times(1)).insertCoaches(any())
    }

    private fun produceTeamResponseSuccess(): Response<TeamResponse> {
        val teamResponse = TeamResponse()
        teamResponse.add(
            0,
            TeamDto(
                produceCoachDtos(),
                producePlayerDtos(),
                "teamBadge3",
                "teamKey3",
                "teamName3"
            )
        )
        return Response.success(teamResponse)
    }

    private fun produceTeamResponseError(): Response<TeamResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }


    private fun producePlayerDtos(): List<PlayerDto> {
        return listOf(
            PlayerDto(
                "playerAge2",
                "playerCountry2",
                "playerGoals2",
                2L,
                "playerMatchPlayed2",
                "playerName2",
                "playerNumber2",
                "playerRedCard2",
                "playerType2",
                "playerYellowCard2"
            )
        )
    }

    private fun produceCoachDtos(): List<CoacheDto> {
        return listOf(
            CoacheDto(
                "coachAge2",
                "coachCountry2",
                "coachName2"
            )
        )
    }
}