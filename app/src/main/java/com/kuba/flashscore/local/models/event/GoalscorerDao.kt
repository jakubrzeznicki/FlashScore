package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.GoalscorerEntity
import com.kuba.flashscore.local.models.entities.event.SubstitutionsEntity

@Dao
interface GoalscorerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>)

    @Query("SELECT * FROM goalscorer_table WHERE goalscorer_match_id = :matchId ")
    suspend fun getGoalscorersFromSpecificMatch(matchId: String): List<GoalscorerEntity>

}