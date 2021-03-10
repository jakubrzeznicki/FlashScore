package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Statistic
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "statistic_table", primaryKeys = ["type", "statistic_match_id"])
data class StatisticEntity(
    @ColumnInfo(name = "statistic_match_id")
    val matchId: String,
    val away: String,
    val home: String,
    val type: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel(): Statistic {
        return Statistic(
            matchId,
            away,
            home,
            type
        )
    }
}