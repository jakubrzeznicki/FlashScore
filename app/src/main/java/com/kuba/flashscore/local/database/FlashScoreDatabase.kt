package com.kuba.flashscore.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*

@Database(
    entities = [CountryEntity::class, LeagueEntity::class, CoachEntity::class, PlayerEntity::class, TeamEntity::class, StandingEntity::class],
    version = 1
)
abstract class FlashScoreDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
    abstract fun coachDao(): CoachDao
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun standingDao(): StandingDao
}