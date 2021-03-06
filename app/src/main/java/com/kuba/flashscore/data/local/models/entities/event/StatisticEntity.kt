package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "statistic_table", primaryKeys = ["type", "statistic_match_id"])
data class StatisticEntity(
    @ColumnInfo(name = "statistic_match_id")
    val matchId: String,
    val away: String,
    val home: String,
    val type: String
) : Parcelable