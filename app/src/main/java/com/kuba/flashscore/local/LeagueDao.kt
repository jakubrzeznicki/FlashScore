package com.kuba.flashscore.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity

@Dao
interface LeagueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @Query("SELECT * FROM leagues_table")
    fun observeAllLeagues(): LiveData<List<LeagueEntity>>

    @Transaction
    @Query("SELECT * FROM countries_table WHERE country_id = :countryId ")
    fun observeLeaguesByCountryId(countryId: String) : LiveData<CountryAndLeagues>
}