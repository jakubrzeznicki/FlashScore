package com.kuba.flashscore.repositories

import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository

class FakeCountryRepository : CountryRepository {

    private val countryItems = mutableListOf<CountryEntity>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun refreshCountries(): Resource<List<CountryEntity>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(countryItems)
        }
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryItems.addAll(countries)
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryItems
    }

//    override suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse> {
//        return if (shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(LeagueResponse())
//        }
//    }
//
//    override suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse> {
//        return if(shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(TeamResponse())
//        }
//    }
//
//    override suspend fun getTeamByTeamId(teamId: String): Resource<TeamResponse> {
//        return if(shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(TeamResponse())
//        }
//    }
//
//    override suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse> {
//        return if(shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(StandingResponse())
//        }
//    }
//
//    override suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse> {
//        return if(shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(PlayerResponse())
//        }
//    }
//
//    override suspend fun getEventsFromSpecificLeagues(
//        leagueId: String,
//        from: String,
//        to: String
//    ): Resource<EventResponse> {
//        return if(shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(EventResponse())
//        }
//    }


}
