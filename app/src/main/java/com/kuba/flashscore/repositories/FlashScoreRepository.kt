package com.kuba.flashscore.repositories

import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface FlashScoreRepository {

    suspend fun getCountries(): Resource<CountryResponse>
    suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse>
    suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse>
    suspend fun getTeamByTeamId(teamId: String): Resource<TeamResponse>
    suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse>
    suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getEventsFromSpecificLeagues(leagueId: String, from: String, to: String): Resource<EventResponse>

}