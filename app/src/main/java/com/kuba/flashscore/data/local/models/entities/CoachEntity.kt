package com.kuba.flashscore.data.local.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscore.data.domain.models.Coach
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong

@Entity(tableName = "coach_table")
data class CoachEntity(
    @ColumnInfo(name = "coach_team_id")
    val teamId: String?,
    @ColumnInfo(name = "coach_age")
    val coachAge: String,
    @ColumnInfo(name = "coach_country")
    val coachCountry: String,
    @ColumnInfo(name = "coach_name")
    @PrimaryKey(autoGenerate = false)
    val coachName: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = dateToLong(
        createTimestamp()
    )
) {
    fun asDomainModel() : Coach {
        return Coach(
            teamId,
            coachAge,
            coachCountry,
            coachName
        )
    }
}