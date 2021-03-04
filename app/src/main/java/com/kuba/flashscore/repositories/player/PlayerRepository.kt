package com.kuba.flashscore.repositories.player

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface PlayerRepository {
    suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getPlayerInformationFromDb(playerId: String): PlayerEntity
    suspend fun getPlayersFromSpecificTeamFromDb(teamId: String): TeamWithPlayersAndCoach
}
