package com.kuba.flashscore.local

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)


    @Query("SELECT * FROM team_table")
    fun getAllTeams(): List<TeamEntity>

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_id = :teamId ")
    suspend fun getTeamByTeamId(teamId: String): TeamWithPlayersAndCoach

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_league_id = :leagueId ")
    suspend fun getTeamsFromSpecificLeague(leagueId: String): List<TeamEntity>
}