package com.kuba.flashscore.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscore.local.CountryDao
import com.kuba.flashscore.local.models.entities.CountryEntity

@Database(
    entities = [CountryEntity::class],
    version = 1
)
abstract class FlashScoreDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
}