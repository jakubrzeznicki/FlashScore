package com.kuba.flashscore.repositories.team

import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceTeamEntity

class FakeTeamRepositoryAndroidTest : TeamRepository {
    private val teamItems = mutableListOf<TeamEntity>()
    private val coachItems = mutableListOf<CoachEntity>()
    private val playerItems = mutableListOf<PlayerEntity>()

    private val countryWithLeagueAndTeamsEntity = CountryWithLeagueAndTeamsEntity(
        produceCountryEntity(1),
        listOf(
            LeagueWithTeamsEntity(
                produceLeagueEntity(1, 1),
                teamItems
            )
        )
    )

    private val teamWithPlayersAndCoachEntity = TeamWithPlayersAndCoachEntity(
        produceTeamEntity(2, 1),
        playerItems,
        coachItems
    )

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertCoaches(coaches: List<CoachEntity>) {
        coachItems.addAll(coaches)
    }

    override suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerItems.addAll(players)
    }

    override suspend fun insertTeams(teams: List<TeamEntity>) {
        teamItems.addAll(teams)
    }

    override suspend fun refreshTeamsFromSpecificLeague(leagueId: String): Resource<CountryWithLeagueAndTeams> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(countryWithLeagueAndTeamsEntity.asDomainModel())
        }
    }

    override suspend fun getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeamsEntity {
        return countryWithLeagueAndTeamsEntity
    }

    override suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoachEntity {
        return teamWithPlayersAndCoachEntity
    }
}
