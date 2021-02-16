package com.kuba.flashscore.repositories

import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

class FakeFlashScoreRepositoryAndroidTest : FlashScoreRepository {

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun getCountries(): Resource<CountryResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(CountryResponse())
        }
    }

    override suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(LeagueResponse())
        }
    }

    override suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(TeamResponse())
        }
    }

    override suspend fun getTeamByTeamId(teamId: String): Resource<TeamResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(TeamResponse())
        }
    }

    override suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(StandingResponse())
        }
    }

    override suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(PlayerResponse())
        }
    }

    override suspend fun getEventsFromSpecificLeagues(
        leagueId: String,
        from: String,
        to: String
    ): Resource<EventResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(EventResponse())
        }
    }
}
