package com.kuba.flashscore.data.local.daos.event

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.StatisticEntity

@Dao
interface StatisticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(statistics: List<StatisticEntity>)

    @Query("SELECT * FROM statistic_table WHERE statistic_match_id = :matchId ")
    suspend fun getStatisticsFromSpecificMatch(matchId: String): List<StatisticEntity>
}