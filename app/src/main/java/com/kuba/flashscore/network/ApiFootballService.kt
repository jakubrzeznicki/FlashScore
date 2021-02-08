package com.kuba.flashscore.network

import com.kuba.flashscore.BuildConfig
import com.kuba.flashscore.network.responses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFootballService {

    @GET("/?action=get_countries")
    suspend fun getCountries(
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<CountryResponse>


    @GET("/?action=get_leagues")
    suspend fun getLeagues(
        @Query("country_id") country_id: String,
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<LeagueResponse>

    @GET("/?action=get_teams")
    suspend fun getTeams(
        @Query("league_id") league_id: String,
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<TeamResponse>

    @GET("/?action=get_teams")
    suspend fun getTeam(
        @Query("team_id") team_id: String,
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<TeamResponse>

    @GET("/?action=get_players")
    suspend fun getPlayer(
        @Query("player_name") player_name: String,
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<PlayerResponse>

    @GET("/?action=get_standings")
    suspend fun getStandings(
        @Query("league_id") league_id: String,
        @Query("APIkey") APIkey: String = BuildConfig.API_KEY
    ): Response<StandingResponse>


}