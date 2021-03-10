package com.kuba.flashscore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.daos.event.*
import com.kuba.flashscore.other.Converters

@Database(
    entities = [CountryEntity::class, LeagueEntity::class, CoachEntity::class, PlayerEntity::class,
        TeamEntity::class, StandingEntity::class, CardEntity::class, GoalscorerEntity::class,
        LineupEntity::class, StatisticEntity::class, SubstitutionsEntity::class, EventEntity::class,
        EventInformationEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
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