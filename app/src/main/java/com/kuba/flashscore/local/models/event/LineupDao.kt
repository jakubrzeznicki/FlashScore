package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.LineupEntity
import com.kuba.flashscore.local.models.entities.event.SubstitutionsEntity

@Dao
interface LineupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLineup(lineups: List<LineupEntity>)

    @Query("SELECT * FROM lineup_table WHERE lineup_match_id = :matchId ")
    suspend fun getLineupsFromSpecificMatch(matchId: String): List<LineupEntity>

}