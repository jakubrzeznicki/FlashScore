package com.kuba.flashscore.repositories

import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class DefaultFlashScoreRepository @Inject constructor(
    private val apiFootballService: ApiFootballService
) : FlashScoreRepository {

    override suspend fun getCountries(): Resource<CountryResponse> {
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<LeagueResponse> {
        return try {
            val response = apiFootballService.getLeagues(countryId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<TeamResponse> {
        return try {
            val response = apiFootballService.getTeams(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
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
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse> {
        return try {
            val response = apiFootballService.getStandings(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }
}