package com.kuba.flashscore.repositories.team

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface TeamRepository {
    suspend fun insertCoaches(coaches: List<CoachEntity>)
    suspend fun insertPlayers(players: List<PlayerEntity>)
    suspend fun insertTeams(teams: List<TeamEntity>)
    suspend fun getTeamsFromSpecificLeagueFromNetwork(leagueId: String): Resource<CountryWithLeagueAndTeams>
    suspend fun getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeams
    suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoach
}
