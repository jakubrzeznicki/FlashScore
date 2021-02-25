package com.kuba.flashscore.local

import androidx.room.*
import com.kuba.flashscore.local.models.entities.CoachEntity

@Dao
interface CoachDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoaches(coaches: List<CoachEntity>)

    @Query("SELECT * FROM coach_table")
    suspend fun getAllCoaches(): List<CoachEntity>

    @Transaction
    @Query("SELECT * FROM coach_table WHERE coach_team_id = :teamId ")
    suspend fun getCoachFromSpecificTeam(teamId: String) : CoachEntity
}