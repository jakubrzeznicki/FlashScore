package com.kuba.flashscore.repositories.standing

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.StandingDtoMapper
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultStandingRepository @Inject constructor(
    private val standingDao: StandingDao,
    private val standingDtoMapper: StandingDtoMapper,
    private val apiFootballService: ApiFootballService
) : StandingRepository {
    override suspend fun insertStandings(standings: List<StandingEntity>) {
        standingDao.insertStandings(standings)
    }


    override suspend fun getStandingsFromSpecificLeagueFromNetwork(leagueId: String): Resource<List<StandingEntity>> {
        return try {
            val response = apiFootballService.getStandings(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    insertStandings(
                        standingDtoMapper.toLocalList(
                            it?.toList()!!,
                            null
                        )
                    )
                    return Resource.success(standingDao.getStandingsFromSpecificLeague(leagueId))

                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity> {
        return standingDao.getStandingsFromSpecificLeague(leagueId)
    }
}