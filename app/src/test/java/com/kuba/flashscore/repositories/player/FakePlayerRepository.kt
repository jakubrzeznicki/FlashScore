package com.kuba.flashscore.repositories.player

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.network.responses.PlayerResponse
import com.kuba.flashscore.other.Resource

class FakePlayerRepository : PlayerRepository {

    private val playerItems = mutableListOf<PlayerEntity>()
    private val teamItem = TeamEntity(
        "leagueId1",
        "teamBadge1",
        "teamId1",
        "teamName1",

        )
    private val playersItems = producePlayerEntities()
    private val coachesItems = produceCoachEntities()

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
        return PlayerEntity(
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

    }

    override suspend fun getPlayersFromSpecificTeamFromDb(teamId: String): TeamWithPlayersAndCoachEntity {
        return teamWithPlayersAndCoachItems
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
