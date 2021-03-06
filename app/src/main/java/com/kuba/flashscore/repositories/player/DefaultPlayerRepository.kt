package com.kuba.flashscore.repositories.player

import com.kuba.flashscore.data.local.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.event.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultPlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val apiFootballService: ApiFootballService
) : PlayerRepository {
    override suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse> {
        return try {
            val response = apiFootballService.getPlayer(name)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getPlayerInformationFromDb(playerId: String): PlayerEntity {
        return playerDao.getPlayersByPlayerId(playerId)
    }

    override suspend fun getPlayersFromSpecificTeamFromDb(teamId: String): TeamWithPlayersAndCoach {
        return playerDao.getPlayersFromSpecificTeam(teamId)
    }
}