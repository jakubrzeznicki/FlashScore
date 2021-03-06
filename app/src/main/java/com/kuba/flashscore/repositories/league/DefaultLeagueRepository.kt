package com.kuba.flashscore.repositories.league

import com.kuba.flashscore.data.local.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.mappers.LeagueDtoMapper
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultLeagueRepository @Inject constructor(
    private val leagueDao: LeagueDao,
    private val leagueDtoMapper: LeagueDtoMapper,
    private val apiFootballService: ApiFootballService
) : LeagueRepository {

    override suspend fun getLeaguesFromSpecificCountryFromNetwork(countryId: String): Resource<CountryAndLeagues> {
        return try {
            val response = apiFootballService.getLeagues(countryId)
            if (response.isSuccessful) {
                response.body().let {
                    insertLeagues(
                        leagueDtoMapper.toLocalList(
                            it?.toList()!!,
                            null
                        )
                    )
                    return Resource.success(leagueDao.getLeaguesFromSpecificCountry(countryId))
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
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