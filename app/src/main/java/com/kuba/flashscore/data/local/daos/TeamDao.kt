package com.kuba.flashscore.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_id = :teamId ")
    suspend fun getTeamByTeamId(teamId: String): TeamWithPlayersAndCoachEntity

    @Transaction
    @Query("SELECT c.* FROM countries_table c JOIN leagues_table l ON c.country_id = l.league_country_id WHERE  l.league_id= :leagueId ")
    suspend fun getTeamsFromSpecificLeague(leagueId: String): CountryWithLeagueAndTeamsEntity
}