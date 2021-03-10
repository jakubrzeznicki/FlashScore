package com.kuba.flashscore.repositories.team

import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.other.Resource

interface TeamRepository {
    suspend fun insertCoaches(coaches: List<CoachEntity>)
    suspend fun insertPlayers(players: List<PlayerEntity>)
    suspend fun insertTeams(teams: List<TeamEntity>)
    suspend fun refreshTeamsFromSpecificLeague(leagueId: String): Resource<CountryWithLeagueAndTeams>
    suspend fun getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeamsEntity
    suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoachEntity
}
