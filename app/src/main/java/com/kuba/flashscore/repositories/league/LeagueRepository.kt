package com.kuba.flashscore.repositories.league

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface LeagueRepository {
    suspend fun getLeaguesFromSpecificCountryFromNetwork(countryId: String): Resource<LeagueResponse>
    suspend fun insertLeagues(leagues: List<LeagueEntity>)
    suspend fun getLeaguesFromDb(): List<LeagueEntity>
    suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeagues
}
