package com.kuba.flashscore.repositories.league

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.other.Resource

interface LeagueRepository {
    suspend fun refreshLeaguesFromSpecificCountry(countryId: String): Resource<CountryAndLeagues>
    suspend fun insertLeagues(leagues: List<LeagueEntity>)
    fun getLeaguesFromDb(): LiveData<List<LeagueEntity>>
    suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeaguesEntity
}
