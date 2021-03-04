package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "goalscorer_table", primaryKeys = ["time", "scorer_id", "goalscorer_match_id"])
data class GoalscorerEntity(
    @ColumnInfo(name = "goalscorer_match_id")
    val matchId: String,
    @ColumnInfo(name = "assist_id")
    val assistId: String,
    @ColumnInfo(name = "scorer_id")
    val scorerId: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val info: String,
    val score: String,
    val time: String,
) : Parcelable