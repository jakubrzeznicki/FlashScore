package com.kuba.flashscore.data.local.models.event

import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.LineupEntity

@Dao
interface LineupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLineup(lineups: List<LineupEntity>)

    @Query("SELECT * FROM lineup_table WHERE lineup_match_id = :matchId ")
    suspend fun getLineupsFromSpecificMatch(matchId: String): List<LineupEntity>

}