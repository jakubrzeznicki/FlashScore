package com.kuba.flashscore.repositories

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultFlashScoreRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val leagueDao: LeagueDao,
    private val coachDao: CoachDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao,
    private val standingDao: StandingDao,
    private val apiFootballService: ApiFootballService
) : FlashScoreRepository {

    override suspend fun getCountriesFromNetwork(): Resource<CountryResponse> {
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

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryDao.insertCountries(countries)
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryDao.getAllCountriesFromDb()
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

    override suspend fun insertLeagues(leagues: List<LeagueEntity>) {
        leagueDao.insertLeagues(leagues)
    }

    override suspend fun getLeaguesFromDb(): List<LeagueEntity> {
        return leagueDao.getAllLeagues()
    }

    override suspend fun getLeagueFromSpecificCountryFromDb(countryId: String): CountryAndLeagues {
        return leagueDao.getLeaguesFromSpecificCountry(countryId)
    }

    override suspend fun insertCoaches(coaches: List<CoachEntity>) {
        coachDao.insertCoaches(coaches)
    }

    override suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerDao.insertPlayers(players)
    }

    override suspend fun insertTeams(teams: List<TeamEntity>) {
        teamDao.insertTeams(teams)
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

    override suspend fun getTeamsFromLeagueFromDb(leagueId: String): List<TeamEntity> {
       return teamDao.getTeamsFromSpecificLeague(leagueId)
    }

    override suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoach {
        return teamDao.getTeamByTeamId(teamId)
    }

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
            val response = apiFootballService.getEvents(from, to, leagueId)
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