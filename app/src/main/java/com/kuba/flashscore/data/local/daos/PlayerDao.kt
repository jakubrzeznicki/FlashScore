package com.kuba.flashscore.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("SELECT * FROM player_table")
    fun getAllPlayers(): LiveData<List<PlayerEntity>>


    @Query("SELECT * FROM player_table WHERE player_id = :playerId ")
    suspend fun getPlayersByPlayerId(playerId: String): PlayerEntity

    @Transaction
    @Query("SELECT * FROM team_table WHERE team_id = :teamId ")
    suspend fun getPlayersFromSpecificTeam(teamId: String): TeamWithPlayersAndCoachEntity
}