package com.kuba.flashscore.data.local

import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.*

@Dao
interface StandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(standings: List<StandingEntity>)

    @Query("SELECT * FROM standing_table WHERE league_id = :leagueId ")
    suspend fun getStandingsFromSpecificLeague(leagueId: String): List<StandingEntity>
}