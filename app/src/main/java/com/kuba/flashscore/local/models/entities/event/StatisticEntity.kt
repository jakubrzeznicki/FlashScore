package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "statistic_table")
data class StatisticEntity(
    @ColumnInfo(name = "statistic_match_id")
    val matchId: String,
    val away: String,
    val home: String,
    val type: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "statistic_id")
    var statisticId: Int = 0
}