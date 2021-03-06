package com.kuba.flashscore.data.local

import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.LeagueEntity

@Dao
interface LeagueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @Query("SELECT * FROM leagues_table")
    fun getAllLeagues(): List<LeagueEntity>

    @Transaction
    @Query("SELECT * FROM countries_table WHERE country_id = :countryId ")
    suspend fun getLeaguesFromSpecificCountry(countryId: String): CountryAndLeagues
}