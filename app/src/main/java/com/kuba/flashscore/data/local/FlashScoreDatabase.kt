package com.kuba.flashscore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.entities.Country

@Database(
    entities = [Country::class],
    version = 1
)
abstract class FlashScoreDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
}