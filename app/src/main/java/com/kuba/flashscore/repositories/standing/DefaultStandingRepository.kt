package com.kuba.flashscore.repositories.standing

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

class DefaultStandingRepository @Inject constructor(
    private val standingDao: StandingDao,
    private val apiFootballService: ApiFootballService
) : StandingRepository {
    override suspend fun insertStandings(standings: List<StandingEntity>) {
        standingDao.insertStandings(standings)
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

    override suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity> {
        return standingDao.getStandingsFromSpecificLeague(leagueId)
    }
}