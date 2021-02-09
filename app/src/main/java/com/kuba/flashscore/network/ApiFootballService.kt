package com.kuba.flashscore.network

import com.kuba.flashscore.BuildConfig
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.API_KEY
import com.kuba.flashscore.other.Constants.COUNTRY_ID
import com.kuba.flashscore.other.Constants.LEAGUE_ID
import com.kuba.flashscore.other.Constants.PATH_GET_COUNTRIES
import com.kuba.flashscore.other.Constants.PATH_GET_LEAGUES
import com.kuba.flashscore.other.Constants.PATH_GET_PLAYERS
import com.kuba.flashscore.other.Constants.PATH_GET_STANDINGS
import com.kuba.flashscore.other.Constants.PATH_GET_TEAMS
import com.kuba.flashscore.other.Constants.PLAYER_NAME
import com.kuba.flashscore.other.Constants.TEAM_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFootballService {

    @GET(PATH_GET_COUNTRIES)
    suspend fun getCountries(
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<CountryResponse>


    @GET(PATH_GET_LEAGUES)
    suspend fun getLeagues(
        @Query(COUNTRY_ID) country_id: String,
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<LeagueResponse>

    @GET(PATH_GET_TEAMS)
    suspend fun getTeams(
        @Query(LEAGUE_ID) league_id: String,
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<TeamResponse>

    @GET(PATH_GET_TEAMS)
    suspend fun getTeam(
        @Query(TEAM_ID) team_id: String,
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<TeamResponse>

    @GET(PATH_GET_PLAYERS)
    suspend fun getPlayer(
        @Query(PLAYER_NAME) player_name: String,
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<PlayerResponse>

    @GET(PATH_GET_STANDINGS)
    suspend fun getStandings(
        @Query(LEAGUE_ID) league_id: String,
        @Query(API_KEY) APIkey: String = BuildConfig.API_KEY
    ): Response<StandingResponse>


}