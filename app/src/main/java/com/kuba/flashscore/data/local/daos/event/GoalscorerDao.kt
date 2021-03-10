package com.kuba.flashscore.data.local.daos.event

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.GoalscorerEntity

@Dao
interface GoalscorerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>)

    @Query("SELECT * FROM goalscorer_table WHERE goalscorer_match_id = :matchId ")
    suspend fun getGoalscorersFromSpecificMatch(matchId: String): List<GoalscorerEntity>

}