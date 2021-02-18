package com.kuba.flashscore.repositories

import androidx.lifecycle.LiveData
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface FlashScoreRepository {

    suspend fun insertCountries(countries: List<CountryEntity>)
    suspend fun getCountriesFromNetwork(): Resource<CountryResponse>
    fun observeAllCountries(): LiveData<List<CountryEntity>>

    suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse>
    suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse>
    suspend fun getTeamByTeamId(teamId: String): Resource<TeamResponse>
    suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse>
    suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getEventsFromSpecificLeagues(
        leagueId: String,
        from: String,
        to: String
    ): Resource<EventResponse>

}