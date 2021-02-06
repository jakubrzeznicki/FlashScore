package com.kuba.flashscore.repositories

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.League
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface FlashScoreRepository {

    suspend fun getCountry(id: String) : Resource<Country>

    suspend fun getCountries(): Resource<List<Country>>
    suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<List<League>>

    suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse>
    suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse>
    suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse>
}