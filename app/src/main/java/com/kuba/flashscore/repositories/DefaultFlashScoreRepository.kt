package com.kuba.flashscore.repositories

import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultFlashScoreRepository @Inject constructor(
    private val apiFootballService: ApiFootballService
) : FlashScoreRepository {


    override suspend fun getCountry(id: String): Resource<CountryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountries(): Resource<CountryResponse> {
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse> {
        return try {
            val response = apiFootballService.getLeagues(countryId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse> {
        return try {
            val response = apiFootballService.getTeams(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamByTeamId(teamId: String): Resource<TeamResponse> {
        return try {
            val response = apiFootballService.getTeam(teamId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }


    override suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse> {
        return try {
            val response = apiFootballService.getStandings(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getPlayerBySpecificName(name: String): Resource<PlayerResponse> {
        return try {
            val response = apiFootballService.getPlayer(name)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getEventsFromSpecificLeagues(
        leagueId: String,
        from: String,
        to: String
    ): Resource<EventResponse> {
        return try {
            val response = apiFootballService.getEvents(from, to,leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

}