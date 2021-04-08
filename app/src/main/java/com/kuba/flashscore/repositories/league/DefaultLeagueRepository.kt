package com.kuba.flashscore.repositories.league

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.local.daos.LeagueDao
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.other.wrapEspressoIdlingResource
import java.lang.Exception
import javax.inject.Inject

class DefaultLeagueRepository @Inject constructor(
    private val leagueDao: LeagueDao,
    private val apiFootballService: ApiFootballService
) : LeagueRepository {

    override suspend fun refreshLeaguesFromSpecificCountry(countryId: String): Resource<CountryAndLeagues> {
        return try {
            val response = apiFootballService.getLeagues(countryId)
            if (response.isSuccessful) {
                response.body().let { leagueResponse ->
                    insertLeagues(
                        leagueResponse?.toList()!!.map { it.asLocalModel() }
                    )
                    return Resource.success(
                        leagueDao.getLeaguesFromSpecificCountry(countryId).asDomainModel()
                    )
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun insertLeagues(leagues: List<LeagueEntity>) {
        wrapEspressoIdlingResource {
            leagueDao.insertLeagues(leagues)
        }
    }

    override suspend fun getLeaguesFromDb(): List<LeagueEntity> =
        wrapEspressoIdlingResource {
            leagueDao.getAllLeagues()
        }


    override suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeaguesEntity {
        wrapEspressoIdlingResource {
            return leagueDao.getLeaguesFromSpecificCountry(countryId)
        }
    }
}