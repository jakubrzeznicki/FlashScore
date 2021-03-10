package com.kuba.flashscore.repositories.standing

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.local.daos.StandingDao
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultStandingRepository @Inject constructor(
    private val standingDao: StandingDao,
    private val apiFootballService: ApiFootballService
) : StandingRepository {
    override suspend fun insertStandings(standings: List<StandingEntity>) {
        standingDao.insertStandings(standings)
    }

    override suspend fun refreshStandingsFromSpecificLeague(leagueId: String): Resource<List<Standing>> {
        return try {
            val response = apiFootballService.getStandings(leagueId)
            if (response.isSuccessful) {
                response.body().let { countryResponse ->
                    insertStandings(
                        countryResponse?.toList()?.map { it.asLocalModelOverall() }!!
                    )
                    insertStandings(
                        countryResponse.toList().map { it.asLocalModelHome() }
                    )
                    insertStandings(
                        countryResponse.toList().map { it.asLocalModelAway() }
                    )
                    return Resource.success(standingDao.getAllStandingsFromSpecificLeague(leagueId).map { it.asDomainModel() })
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String, standingType: StandingType): List<StandingEntity> {
        return standingDao.getStandingsFromSpecificLeague(leagueId, standingType)
    }

    override suspend fun getAllStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity> {
        return standingDao.getAllStandingsFromSpecificLeague(leagueId)
    }
}