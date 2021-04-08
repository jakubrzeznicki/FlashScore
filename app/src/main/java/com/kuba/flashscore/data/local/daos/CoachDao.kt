package com.kuba.flashscore.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.CoachEntity

@Dao
interface CoachDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoaches(coaches: List<CoachEntity>)

    @Query("SELECT * FROM coach_table")
    suspend fun getAllCoaches(): List<CoachEntity>

    @Query("SELECT * FROM coach_table WHERE coach_team_id = :teamId ")
    suspend fun getCoachFromSpecificTeam(teamId: String): CoachEntity
}