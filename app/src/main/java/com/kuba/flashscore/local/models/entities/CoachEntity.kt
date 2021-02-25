package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val coachName: String
): Parcelable