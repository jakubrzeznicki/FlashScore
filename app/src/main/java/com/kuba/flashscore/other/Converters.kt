package com.kuba.flashscore.other

import androidx.room.TypeConverter
import com.kuba.flashscore.data.local.models.entities.StandingType

class Converters {
    @TypeConverter
    fun toStringStandingType(standingType: StandingType): String {
        return when (standingType) {
            StandingType.OVERALL -> StandingType.OVERALL.toString()
            StandingType.HOME -> StandingType.HOME.toString()
            else -> StandingType.AWAY.toString()
        }
    }

    @TypeConverter
    fun fromStringGender(standingString: String): StandingType {
        return when (standingString) {
            StandingType.OVERALL.toString() -> StandingType.OVERALL
            StandingType.HOME.toString() -> StandingType.HOME
            else -> StandingType.AWAY
        }

    }
}