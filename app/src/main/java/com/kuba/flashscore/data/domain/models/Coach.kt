package com.kuba.flashscore.data.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coach(
    val teamId: String?,
    val coachAge: String,
    val coachCountry: String,
    val coachName: String
): Parcelable