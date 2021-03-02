package com.kuba.flashscore.repositories.league

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultLeagueRepository @Inject constructor(
    private val leagueDao: LeagueDao,
    private val apiFootballService: ApiFootballService
) : LeagueRepository {

    override suspend fun getLeaguesFromSpecificCountryFromNetwork(countryId: String): Resource<LeagueResponse> {
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

    override suspend fun insertLeagues(leagues: List<LeagueEntity>) {
        leagueDao.insertLeagues(leagues)
    }

    override suspend fun getLeaguesFromDb(): List<LeagueEntity> {
        return leagueDao.getAllLeagues()
    }

    override suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeagues {
        return leagueDao.getLeaguesFromSpecificCountry(countryId)
    }
}