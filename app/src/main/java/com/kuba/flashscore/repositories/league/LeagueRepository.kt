package com.kuba.flashscore.repositories.league

import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.other.Resource

interface LeagueRepository {
    suspend fun getLeaguesFromSpecificCountryFromNetwork(countryId: String): Resource<CountryAndLeagues>
    suspend fun insertLeagues(leagues: List<LeagueEntity>)
    suspend fun getLeaguesFromDb(): List<LeagueEntity>
    suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeagues
}
