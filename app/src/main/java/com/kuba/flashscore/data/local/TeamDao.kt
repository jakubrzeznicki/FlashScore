package com.kuba.flashscore.data.local

import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.*

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_id = :teamId ")
    suspend fun getTeamByTeamId(teamId: String): TeamWithPlayersAndCoach

    @Transaction
    @Query("SELECT c.* FROM countries_table c JOIN leagues_table l ON c.country_id = l.league_country_id WHERE  l.league_id= :leagueId ")
    suspend fun getTeamsFromSpecificLeague(leagueId: String): CountryWithLeagueAndTeams
}