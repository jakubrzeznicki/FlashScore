package com.kuba.flashscore.data.local.models.event

import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.SubstitutionsEntity

@Dao
interface SubstitutionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>)

    @Query("SELECT * FROM substitution_table WHERE substitutions_match_id = :matchId ")
    suspend fun getSubstitutionsFromSpecificMatch(matchId: String): List<SubstitutionsEntity>
}