package com.kuba.flashscore.repositories.team

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.daos.CoachDao
import com.kuba.flashscore.data.local.daos.PlayerDao
import com.kuba.flashscore.data.local.daos.TeamDao
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class DefaultTeamRepository @Inject constructor(
    private val coachDao: CoachDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao,
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

    override suspend fun refreshTeamsFromSpecificLeague(leagueId: String): Resource<CountryWithLeagueAndTeams> {
        return try {
            val response = apiFootballService.getTeams(leagueId)
            if (response.isSuccessful) {
                response.body().let { teamResponse ->
                    teamResponse?.toList()?.map { it.asLocalModel(leagueId) }
                        ?.let { insertTeams(it) }

                    teamResponse?.toList()?.forEach { teamDtoItem ->
                        insertPlayers(teamDtoItem.players.map { it.asLocalModel(leagueId) })
                        insertCoaches(teamDtoItem.coaches.map { it.asLocalModel(leagueId) })
                    }
                    Resource.success(teamDao.getTeamsFromSpecificLeague(leagueId).asDomainModel())
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeamsEntity {
        return teamDao.getTeamsFromSpecificLeague(leagueId)
    }

    override suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoachEntity {
        val data = teamDao.getTeamByTeamId(teamId)
        Timber.d("PLAYERS in repo ${data.players.size}")
        return data
    }
}