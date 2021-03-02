package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.StatisticEntity
import com.kuba.flashscore.local.models.entities.event.SubstitutionsEntity

@Dao
interface StatisticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(statistics: List<StatisticEntity>)

    @Query("SELECT * FROM statistic_table WHERE statistic_match_id = :matchId ")
    suspend fun getStatisticsFromSpecificMatch(matchId: String): List<StatisticEntity>
}