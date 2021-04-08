package com.kuba.flashscore.repositories.league

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.other.Resource

class FakeLeagueRepository : LeagueRepository {

    private val leagueItems = mutableListOf<LeagueEntity>()
    private val countryAndLeagueItems = CountryAndLeaguesEntity(
        CountryEntity("countryId1", "countryLogo1", "countryName1"),
        leagueItems
    )

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun refreshLeaguesFromSpecificCountry(countryId: String): Resource<CountryAndLeagues> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(countryAndLeagueItems.asDomainModel())
        }
    }

    override suspend fun insertLeagues(leagues: List<LeagueEntity>) {
        leagueItems.addAll(leagues)
    }

    override suspend fun getLeaguesFromDb(): List<LeagueEntity> {
        return leagueItems
    }

    override suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeaguesEntity {
        return countryAndLeagueItems
    }
}
