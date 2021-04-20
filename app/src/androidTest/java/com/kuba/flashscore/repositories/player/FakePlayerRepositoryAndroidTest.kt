package com.kuba.flashscore.repositories.player

import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.network.responses.PlayerResponse
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.util.DataProducerAndroid.produceCoachEntity
import com.kuba.flashscore.util.DataProducerAndroid.producePlayerEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceTeamEntity

class FakePlayerRepositoryAndroidTest : PlayerRepository {
    private val playerItems = mutableListOf<PlayerEntity>()
    private val teamItem = produceTeamEntity(1, 1)
    private val playersItems = listOf(producePlayerEntity(1,1))
    private val coachesItems = listOf(produceCoachEntity(1,1))

    private val teamWithPlayersAndCoachItems = TeamWithPlayersAndCoachEntity(
        teamItem,
        playersItems,
        coachesItems
    )

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }


    override suspend fun refreshPlayerBySpecificName(name: String): Resource<PlayerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlayerInformationFromDb(playerId: Long): PlayerEntity {
        return producePlayerEntity(1,2)

    }

    override suspend fun getPlayersFromSpecificTeamFromDb(teamId: String): TeamWithPlayersAndCoachEntity {
        return teamWithPlayersAndCoachItems
    }
}
