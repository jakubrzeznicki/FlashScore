package com.kuba.flashscore.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuba.flashscore.data.local.entities.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryItem(country: Country)

    @Query("SELECT * FROM country")
    fun observeCountryItem(): LiveData<List<Country>>
}