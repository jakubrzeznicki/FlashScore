package com.kuba.flashscore.repositories.player

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.network.responses.*
import com.kuba.flashscore.other.Resource

interface PlayerRepository {
    suspend fun refreshPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getPlayerInformationFromDb(playerId: String): PlayerEntity
    suspend fun getPlayersFromSpecificTeamFromDb(teamId: String): TeamWithPlayersAndCoachEntity
}
