package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Statistic(
    val matchId: String,
    val away: String,
    val home: String,
    val type: String
) : Parcelable