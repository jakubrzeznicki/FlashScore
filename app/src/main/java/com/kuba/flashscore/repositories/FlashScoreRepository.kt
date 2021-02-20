package com.kuba.flashscore.repositories

import androidx.lifecycle.LiveData
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface FlashScoreRepository {

    suspend fun insertCountries(countries: List<CountryEntity>)
    suspend fun getCountriesFromNetwork(): Resource<CountryResponse>
    fun getCountriesFromDb(): List<CountryEntity>

    suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse>
    suspend fun insertLeagues(leagues: List<LeagueEntity>)
    fun observeAllLeagues(): LiveData<List<LeagueEntity>>
    fun observeLeagueByCountryId(countryId: String): LiveData<CountryAndLeagues>

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