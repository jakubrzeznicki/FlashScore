package com.kuba.flashscore.repositories.team

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.CoachDtoMapper
import com.kuba.flashscore.network.mappers.PlayerDtoMapper
import com.kuba.flashscore.network.mappers.TeamDtoMapper
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultTeamRepository @Inject constructor(
    private val coachDao: CoachDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao,
    private val teamDtoMapper: TeamDtoMapper,
    private val coachDtoMapper: CoachDtoMapper,
    private val playerDtoMapper: PlayerDtoMapper,
    private val apiFootballService: ApiFootballService
) : TeamRepository {
    override suspend fun insertCoaches(coaches: List<CoachEntity>) {
        coachDao.insertCoaches(coaches)
    }

    override suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerDao.insertPlayers(players)
    }

    override suspend fun insertTeams(teams: List<TeamEntity>) {
        teamDao.insertTeams(teams)
    }

    override suspend fun getTeamsFromSpecificLeagueFromNetwork(leagueId: String): Resource<CountryWithLeagueAndTeams> {
        return try {
            val response = apiFootballService.getTeams(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    insertTeams(
                        teamDtoMapper.toLocalList(
                            it?.toList()!!,
                            leagueId
                        )
                    )

                    it.toList().forEach { teamDtoItem ->
                        insertPlayers(
                            playerDtoMapper.toLocalList(
                                teamDtoItem.players,
                                teamDtoItem.teamKey
                            )
                        )

                        insertCoaches(
                            coachDtoMapper.toLocalList(
                                teamDtoItem.coaches,
                                teamDtoItem.teamKey
                            )
                        )
                    }
                    Resource.success(teamDao.getTeamsFromSpecificLeague(leagueId))
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamsFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeams {
        return teamDao.getTeamsFromSpecificLeague(leagueId)
    }

    override suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoach {
        return teamDao.getTeamByTeamId(teamId)
    }
}