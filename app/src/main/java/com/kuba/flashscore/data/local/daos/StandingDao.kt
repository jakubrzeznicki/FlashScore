package com.kuba.flashscore.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.*

@Dao
interface StandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(standings: List<StandingEntity>)

    @Query("SELECT * FROM standing_table WHERE league_id = :leagueId AND standing_type = :standingType")
    suspend fun getStandingsFromSpecificLeague(leagueId: String, standingType: StandingType): List<StandingEntity>

    @Query("SELECT * FROM standing_table WHERE league_id = :leagueId")
    suspend fun getAllStandingsFromSpecificLeague(leagueId: String): List<StandingEntity>
}
