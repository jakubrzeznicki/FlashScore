package com.kuba.flashscore.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.local.models.entities.*

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("SELECT * FROM player_table")
    suspend fun getAllPlayers(): List<PlayerEntity>


    @Query("SELECT * FROM player_table WHERE player_id = :playerId ")
    suspend fun getPlayersByPlayerId(playerId: String): PlayerEntity

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_id = :teamId ")
    suspend fun getPlayersFromSpecificTeam(teamId: String): TeamWithPlayersAndCoach
}