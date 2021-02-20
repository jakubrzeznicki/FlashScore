package com.kuba.flashscore.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscore.local.CountryDao
import com.kuba.flashscore.local.LeagueDao
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity

@Database(
    entities = [CountryEntity::class, LeagueEntity::class, ],
    version = 1
)
abstract class FlashScoreDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
}