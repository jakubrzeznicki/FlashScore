package com.kuba.flashscore.repositories

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface FlashScoreRepository {

    suspend fun insertCountries(countries: List<CountryEntity>)
    suspend fun getCountriesFromNetwork(): Resource<CountryResponse>
    suspend fun getCountriesFromDb(): List<CountryEntity>

    suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse>
    suspend fun insertLeagues(leagues: List<LeagueEntity>)
    suspend fun getLeaguesFromDb(): List<LeagueEntity>
    suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeagues

    suspend fun insertCoaches(coaches: List<CoachEntity>)
    suspend fun insertPlayers(players: List<PlayerEntity>)
    suspend fun insertTeams(teams: List<TeamEntity>)
    suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse>
    suspend fun getTeamsFromLeagueFromDb(leagueId: String): List<TeamEntity>
    suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoach

    suspend fun insertStandings(standings: List<StandingEntity>)
    suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse>
    suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity>

    suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getEventsFromSpecificLeagues(
        leagueId: String,
        from: String,
        to: String
    ): Resource<EventResponse>

}