package com.kuba.flashscore.repositories

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.entities.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class DefaultFlashScoreRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val apiFootballService: ApiFootballService,
    private val countryMapper: CountryDtoMapper,
    private val leagueMapper: LeagueDtoMapper,
    private val teamMapper: TeamDtoMapper,
    private val standingMapper: StandingDtoMapper

) : FlashScoreRepository {


    override suspend fun getCountry(id: String): Resource<Country> {
        TODO("Not yet implemented")
    }


    override suspend fun getCountries(): Resource<List<Country>> {
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it?.toList()?.let { countryDto -> countryMapper.toLocalList(countryDto) })
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getLeaguesFromSpecificCountry(countryId: String): Resource<List<League>> {
        return try {
            val response = apiFootballService.getLeagues(countryId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it?.toList()?.let { leagueDto -> leagueMapper.toLocalList(leagueDto) })
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamsFromSpecificLeague(leagueId: String): Resource<List<Team>> {
        return try {
            val response = apiFootballService.getTeams(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it?.toList()?.let { teamDto -> teamMapper.toLocalList(teamDto) })
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun getTeamByTeamId(teamId: String): Resource<Team> {
        return try {
            val response = apiFootballService.getTeam(teamId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it?.get(0).let { teamDto -> teamMapper.mapToLocalModel(teamDto!!) })
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Timber.e("EXCEPTION: $e")
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }


    override suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<List<Standing>> {
        return try {
            val response = apiFootballService.getStandings(leagueId)
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it?.toList()
                            ?.let { standingDto -> standingMapper.toLocalList(standingDto) })
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


}