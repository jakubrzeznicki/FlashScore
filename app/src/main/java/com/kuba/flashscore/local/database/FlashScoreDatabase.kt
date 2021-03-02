package com.kuba.flashscore.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*

@Database(
    entities = [CountryEntity::class, LeagueEntity::class, CoachEntity::class, PlayerEntity::class,
        TeamEntity::class, StandingEntity::class, CardEntity::class, GoalscorerEntity::class,
        LineupEntity::class, StatisticEntity::class, SubstitutionsEntity::class, EventEntity::class],
    version = 1
)
abstract class FlashScoreDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun leagueDao(): LeagueDao
    abstract fun coachDao(): CoachDao
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun standingDao(): StandingDao
    abstract fun cardDao(): CardDao
    abstract fun goalscorerDao(): GoalscorerDao
    abstract fun lineupDao(): LineupDao
    abstract fun statisticDao(): StatisticDao
    abstract fun substitutionDao(): SubstitutionDao
    abstract fun eventDao(): EventDao
}